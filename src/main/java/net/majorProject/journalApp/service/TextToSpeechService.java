package net.majorProject.journalApp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
        import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class TextToSpeechService {

    @Value("${elevenlabs.api.key}")
    private String apiKey;

    private static final String VOICE_ID =
            "JBFqnCBsd6RMkjVDRZzb";

    public byte[] convertTextToSpeech(String text) {

        String url =
                "https://api.elevenlabs.io/v1/text-to-speech/"
                        + VOICE_ID;

        RestTemplate restTemplate = new RestTemplate();

        // Headers
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(
                MediaType.APPLICATION_JSON
        );

        headers.set(
                "xi-api-key",
                apiKey
        );

        headers.setAccept(
                Collections.singletonList(
                        MediaType.APPLICATION_OCTET_STREAM
                )
        );

        // Request Body
        Map<String, Object> requestBody =
                new HashMap<>();

        requestBody.put("text", text);

        requestBody.put(
                "model_id",
                "eleven_multilingual_v2"
        );

        // Optional voice settings
        Map<String, Object> voiceSettings =
                new HashMap<>();

        voiceSettings.put("stability", 0.5);

        voiceSettings.put(
                "similarity_boost",
                0.5
        );

        requestBody.put(
                "voice_settings",
                voiceSettings
        );

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(
                        requestBody,
                        headers
                );

        ResponseEntity<byte[]> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        entity,
                        byte[].class
                );

        return response.getBody();
    }
}