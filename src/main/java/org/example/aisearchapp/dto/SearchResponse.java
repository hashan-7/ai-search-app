package org.example.aisearchapp.dto;

import java.util.List;

public class SearchResponse {

    private String summary;
    private List<String> sources;
    private List<String> followUpQuestions;

    public SearchResponse() {
    }

    public SearchResponse(String summary, List<String> sources, List<String> followUpQuestions) {
        this.summary = summary;
        this.sources = sources;
        this.followUpQuestions = followUpQuestions;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<String> getSources() {
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }

    public List<String> getFollowUpQuestions() {
        return followUpQuestions;
    }

    public void setFollowUpQuestions(List<String> followUpQuestions) {
        this.followUpQuestions = followUpQuestions;
    }
}
