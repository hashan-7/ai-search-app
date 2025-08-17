package org.example.aisearchapp.api;

import org.example.aisearchapp.dto.SearchResponse;
import org.example.aisearchapp.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class SearchApiController {

    private final SearchService searchService;

    @Autowired
    public SearchApiController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping("/search")
    public SearchResponse handleSearch(@RequestBody Map<String, String> payload, @AuthenticationPrincipal OAuth2User principal) {
        String query = payload.get("query");
        String userEmail = principal.getAttribute("email");
        return searchService.getAiSummary(query, userEmail);
    }
}
