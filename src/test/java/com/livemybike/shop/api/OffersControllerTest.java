package com.livemybike.shop.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OffersControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void listOffersTest() throws JsonProcessingException, JSONException {
        ResponseEntity<String> response = restTemplate.getForEntity("/offers?pageNumber=1", String.class);
        JSONObject jsonResponse = new JSONObject(response.getBody());

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        assertThat(jsonResponse.getJSONArray("content").getJSONObject(0).getString("title")
                , equalTo("Haibaike big curve"));
    }

}
