package org.example.aisearchapp.service;

import org.example.aisearchapp.dto.SearchResponse;
import org.example.aisearchapp.model.SearchHistory;
import org.example.aisearchapp.model.User;
import org.example.aisearchapp.repository.SearchHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {

    @Value("${google.search.api.key}")
    private String googleApiKey;

    @Value("${google.search.engine.id}")
    private String searchEngineId;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final SearchHistoryRepository searchHistoryRepository;
    private final UserService userService;

    @Autowired
    public SearchService(SearchHistoryRepository searchHistoryRepository, UserService userService) {
        this.searchHistoryRepository = searchHistoryRepository;
        this.userService = userService;
    }

    @Transactional
    public SearchResponse getAiSummary(String query, String userEmail) {
        User user = userService.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found for email: " + userEmail));

        List<String> links = searchGoogle(query);
        if (links.isEmpty()) {
            return new SearchResponse("Sorry, I couldn't find any relevant information for your query from allowed sources.", new ArrayList<>(), new ArrayList<>());
        }

        String context = scrapeLinks(links);
        if (context.trim().isEmpty()) {
            return new SearchResponse("Sorry, I was unable to retrieve content from the top search results as they are protected.", links, new ArrayList<>());
        }

        String summary = callGeminiApiForSummary(query, context);
        List<String> followUpQuestions = new ArrayList<>();

        if (!summary.startsWith("Sorry,")) {
            followUpQuestions = generateFollowUpQuestions(query, summary);
            SearchHistory history = new SearchHistory(query, summary, LocalDateTime.now(), user);
            searchHistoryRepository.save(history);
        }

        return new SearchResponse(summary, links, followUpQuestions);
    }

    public List<SearchHistory> getSearchHistoryForUser(String userEmail) {
        User user = userService.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found for email: " + userEmail));
        return searchHistoryRepository.findByUserOrderBySearchTimestampDesc(user);
    }

    @Transactional
    public void deleteHistoryItemById(Long id) {
        searchHistoryRepository.deleteById(id);
    }

    @Transactional
    public void clearHistoryForUser(String userEmail) {
        User user = userService.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found for email: " + userEmail));
        searchHistoryRepository.deleteByUser(user);
    }

    private List<String> searchGoogle(String query) {
        String url = "https://www.googleapis.com/customsearch/v1?key=" + googleApiKey + "&cx=" + searchEngineId + "&q=" + query;
        List<String> links = new ArrayList<>();
        try {
            String response = restTemplate.getForObject(url, String.class);
            JSONObject jsonResponse = new JSONObject(response);
            if (jsonResponse.has("items")) {
                JSONArray items = jsonResponse.getJSONArray("items");
                for (int i = 0; i < items.length() && links.size() < 7; i++) {
                    String link = items.getJSONObject(i).getString("link");
                    if (!link.contains("reddit.com") && !link.contains("quora.com")) {
                        links.add(link);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("!!! ERROR searching Google: " + e.getMessage());
        }
        return links;
    }

    private String scrapeLinks(List<String> links) {
        StringBuilder content = new StringBuilder();
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36";

        for (String link : links) {
            try {
                Document doc = Jsoup.connect(link)
                        .userAgent(userAgent)
                        .referrer("https://www.google.com/")
                        .header("Accept-Language", "en-US,en;q=0.5")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                        .timeout(10000)
                        .get();
                content.append(doc.body().text()).append("\n\n");
            } catch (IOException e) {
                System.err.println("!!! ERROR scraping link " + link + ": " + e.getMessage());
            }
        }
        return content.toString();
    }

    private String callGeminiApiForSummary(String query, String context) {
        String prompt = "You are an intelligent research assistant. Your task is to analyze the content provided from multiple websites below. " +
                "First, identify the key facts and points that are mentioned repeatedly across several of these sources. " +
                "Then, using these commonly agreed-upon facts as the foundation, write a comprehensive and well-structured note that fully answers the user's question: '" + query + "'. " +
                "The note should be accurate, detailed, and easy to understand.\n\n" +
                "Provided Information:\n" + context;
        return callGeminiApi(prompt);
    }

    private List<String> generateFollowUpQuestions(String originalQuery, String summary) {
        String prompt = "Based on the original question '" + originalQuery + "' and the provided summary, suggest three short, relevant follow-up questions a user might ask next. " +
                "Return ONLY the three questions separated by a semicolon (;). Do not add numbers or any other text.";
        String response = callGeminiApi(prompt);
        if (response.startsWith("Sorry,")) {
            return new ArrayList<>();
        }
        return Arrays.stream(response.split(";"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    private String callGeminiApi(String prompt) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=" + geminiApiKey;

        JSONObject textPart = new JSONObject();
        textPart.put("text", prompt);
        JSONArray parts = new JSONArray();
        parts.put(textPart);
        JSONObject content = new JSONObject();
        content.put("parts", parts);
        JSONArray contents = new JSONArray();
        contents.put(content);
        JSONObject requestBody = new JSONObject();
        requestBody.put("contents", contents);

        try {
            String response = restTemplate.postForObject(url, requestBody.toString(), String.class);
            JSONObject jsonResponse = new JSONObject(response);
            if (jsonResponse.has("candidates")) {
                return jsonResponse.getJSONArray("candidates").getJSONObject(0).getJSONObject("content").getJSONArray("parts").getJSONObject(0).getString("text");
            } else {
                return "Sorry, the AI could not generate a valid response.";
            }
        } catch (Exception e) {
            System.err.println("!!! ERROR calling Gemini API: " + e.getMessage());
            return "Sorry, there was an error communicating with the AI.";
        }
    }
}
