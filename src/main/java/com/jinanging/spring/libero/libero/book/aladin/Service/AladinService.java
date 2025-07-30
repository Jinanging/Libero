package com.jinanging.spring.libero.libero.book.aladin.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class AladinService {

	private final RestTemplate restTemplate;
    private final String ttbKey;

    public AladinService(RestTemplate restTemplate, @Value("${aladin.ttb-key}") String ttbKey) {
        this.restTemplate = restTemplate;
        this.ttbKey = ttbKey;
    }
    //경고 무시
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getBooksByCategory(int categoryId) {
        String url = UriComponentsBuilder.fromUriString("https://www.aladin.co.kr/ttb/api/ItemList.aspx")
                .queryParam("ttbkey", ttbKey)
                .queryParam("QueryType", "ItemNewAll")
                .queryParam("MaxResults", 10)
                .queryParam("start", 1)
                .queryParam("SearchTarget", "Book")
                .queryParam("CategoryId", categoryId)
                .queryParam("output", "js")
                .queryParam("Version", "20131101")
                .toUriString();
        
        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            Map<String, Object> body = response.getBody();
            
            if (body != null && body.containsKey("item")) {
                return (List<Map<String, Object>>) body.get("item");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    
    
}
