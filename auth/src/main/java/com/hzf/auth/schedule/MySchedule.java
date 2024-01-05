package com.hzf.auth.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class MySchedule {

    @Value("${wechat.url}")
    private String url;

    @Value("${wechat.webhook}")
    private String webhook;

    public void task() {
        String message = "你好，我是机器人";
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = String.format(url, webhook);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String textMsg = "{\"msgtype\":\"text\",\"text\":{\"content\":\"" + message + "\"},\"at\":{\"atMobiles\":[\"xxx\"],\"isAtAll\":false}}";
        HttpEntity<String> requestEntity = new HttpEntity<>(textMsg, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(apiUrl, requestEntity, String.class);
        String responseBody = responseEntity.getBody();
        log.debug(responseBody);
    }

}
