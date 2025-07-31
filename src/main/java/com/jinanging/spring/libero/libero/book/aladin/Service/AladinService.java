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
    
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getBooksByKeyword(String keyword) {
        String url = UriComponentsBuilder.fromUriString("http://www.aladin.co.kr/ttb/api/ItemSearch.aspx")
                .queryParam("ttbkey", ttbKey)
                .queryParam("Query", keyword)
                .queryParam("QueryType", "Keyword")
                .queryParam("SearchTarget", "Book")
                .queryParam("MaxResults", 10)
                .queryParam("start", 1)
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
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    
    
}
