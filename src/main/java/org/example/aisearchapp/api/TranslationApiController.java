package org.example.aisearchapp.api;

import org.example.aisearchapp.dto.TranslationRequest;
import org.example.aisearchapp.service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class TranslationApiController {

    private final TranslationService translationService;

    @Autowired
    public TranslationApiController(TranslationService translationService) {
        this.translationService = translationService;
    }

    @PostMapping("/translate")
    public Map<String, String> handleTranslation(@RequestBody TranslationRequest request) {
        String translatedText = translationService.translateText(request.getText(), request.getTargetLanguage());
        return Map.of("translatedText", translatedText);
    }
}
