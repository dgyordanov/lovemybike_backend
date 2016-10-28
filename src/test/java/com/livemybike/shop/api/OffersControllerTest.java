package com.livemybike.shop.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.livemybike.shop.ShopServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ShopServer.class)
@WebIntegrationTest
public class OffersControllerTest {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String BASE_URL = "http://localhost:8080/offers";
    private RestTemplate template = new TestRestTemplate();

    @Test
    public void listOffersTest() throws JsonProcessingException {
        ResponseEntity<Map[]> response = template.getForEntity(BASE_URL, Map[].class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        Map[] responseBody = response.getBody();
        assertThat(responseBody.length, equalTo(7));
        assertThat(responseBody[0].get("title"), equalTo("Haibaike big curve"));
    }

}
