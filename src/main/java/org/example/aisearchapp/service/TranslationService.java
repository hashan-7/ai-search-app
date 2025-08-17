package org.example.aisearchapp.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TranslationService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String translateText(String textToTranslate, String targetLanguage) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=" + geminiApiKey;

        String prompt = "Translate the following text into " + targetLanguage + ". Only provide the translated text, with no additional explanation or introductory phrases.\n\n" +
                "Text to translate:\n\"" + textToTranslate + "\"";

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
                return "Sorry, the translation could not be completed.";
            }
        } catch (Exception e) {
            System.err.println("!!! ERROR calling Gemini API for translation: " + e.getMessage());
            return "Sorry, there was an error during the translation process.";
        }
    }
}
