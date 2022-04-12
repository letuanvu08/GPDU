package hcmut.thesis.gpduserver.service.http;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j(topic = "HTTP")
public class HttpService {

  private final RestTemplate restTemplate;

  public HttpService() {
    restTemplate = new RestTemplate();
  }

  public String sendGet(String url, HttpHeaders headers) {
    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
    return restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class).getBody();
  }

  public <T> String sendPost(String url, T body, HttpHeaders headers) {
    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
    return restTemplate.postForEntity(url, body, String.class).getBody();
  }
}
