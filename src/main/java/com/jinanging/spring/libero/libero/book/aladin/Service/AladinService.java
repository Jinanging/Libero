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

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AladinService {

    private final RestTemplate restTemplate;
    private final String ttbKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public AladinService(RestTemplate restTemplate, @Value("${aladin.ttb-key}") String ttbKey) {
        this.restTemplate = restTemplate;
        this.ttbKey = ttbKey;
    }
    
    public Map<String, Object> getBookById(long itemId) {
        List<Map<String, Object>> result = getBooksById(itemId);
        if (!result.isEmpty()) {
            Map<String, Object> book = result.get(0);

            // 여기에 로그 추가 (콘솔에 찍힘)
            System.out.println("=== API 응답 book 데이터 ===");
            book.forEach((k,v) -> System.out.println(k + " : " + v));

            return book;
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getBooksById(long itemId) {
        String url = UriComponentsBuilder.fromUriString("http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx")
                .queryParam("ttbkey", ttbKey)
                .queryParam("ItemId", itemId)
                .queryParam("ItemIdType", "ItemId")
                .queryParam("Cover", "Mid")
                .queryParam("output", "js")
                .queryParam("Optresult","ratingInfo")
                .queryParam("Version", "20131101")
                .build()
                .toUriString();

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
            String body = response.getBody();


            if (body != null) {
                Map<String, Object> map = objectMapper.readValue(body, Map.class);
                if (map.containsKey("item")) {
                    return (List<Map<String, Object>>) map.get("item");
                }
            }
        } catch (Exception e) {
            System.out.println("API 호출 또는 JSON 파싱 중 오류 발생:");
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
    
    

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getBooksByKeyword(String keyword, int page) {
        String url = UriComponentsBuilder.fromUriString("http://www.aladin.co.kr/ttb/api/ItemSearch.aspx")
                .queryParam("ttbkey", ttbKey)
                .queryParam("Query", keyword)
                .queryParam("QueryType", "Keyword")
                .queryParam("SearchTarget", "Book")
                .queryParam("MaxResults", 10)
                .queryParam("start", page)  // 페이지 반영
                .queryParam("output", "js")
                .queryParam("Version", "20131101")
                .build()
                .toUriString();

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
            String body = response.getBody();
            
            if (body != null) {
                Map<String, Object> map = objectMapper.readValue(body, Map.class);
                if (map.containsKey("item")) {
                    return (List<Map<String, Object>>) map.get("item");
                }
            }
        } catch (Exception e) {
            System.out.println("API 호출 또는 JSON 파싱 중 오류 발생:");
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getBooksByCategory(int categoryId, int page) {
        String url = UriComponentsBuilder.fromUriString("https://www.aladin.co.kr/ttb/api/ItemList.aspx")
                .queryParam("ttbkey", ttbKey)
                .queryParam("QueryType", "ItemNewAll")
                .queryParam("MaxResults", 10)
                .queryParam("start", page)  // 페이지 반영
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