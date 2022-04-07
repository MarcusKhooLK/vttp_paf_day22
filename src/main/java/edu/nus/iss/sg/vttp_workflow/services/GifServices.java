package edu.nus.iss.sg.vttp_workflow.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class GifServices {

    @Value("${giphy.api.key}")
    private String giphyKey;

    private final String BASE_URL = "https://api.giphy.com/v1/gifs%s";
    private final String SEARCH_RESOURCE = "/search";

    public List<String> searchGifImages(String query) {
        return searchGifImages(query, 10, "PG-13");
    }

    public List<String> searchGifImages(String query, Integer limit) {
        return searchGifImages(query, limit, "PG-13");
    }

    public List<String> searchGifImages(String query, String rating) {
        return searchGifImages(query, 10, rating);
    }
    
    public List<String> searchGifImages(String query, Integer limit, String rating) {
        List<String> result = new ArrayList<>();

        String url = UriComponentsBuilder.fromUriString(BASE_URL.formatted(SEARCH_RESOURCE))
                                        .queryParam("api_key", giphyKey)
                                        .queryParam("q", query)
                                        .queryParam("limit", limit)
                                        .queryParam("rating", rating)
                                        .toUriString();
        
        RequestEntity<Void> req = RequestEntity.get(url)
                                        .accept(MediaType.APPLICATION_JSON)
                                        .build();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resp = restTemplate.exchange(req, String.class);;

        InputStream is = new ByteArrayInputStream(resp.getBody().getBytes());
        JsonReader reader = Json.createReader(is);
        JsonArray jArray = reader.readObject().getJsonArray("data");
        for(int i = 0; i < jArray.size(); i++) {
            JsonObject jObj = jArray.getJsonObject(i);
            JsonObject images = jObj.getJsonObject("images");
            JsonObject fixedWidth = images.getJsonObject("fixed_width");
            String imageUrl = fixedWidth.getString("url");
            result.add(imageUrl);
        }
        return result;
    }

}
