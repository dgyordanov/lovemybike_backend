package com.livemybike.shop.offers.booking;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class BookingRESTApiTest extends AbstractBookingTest {

    public static final String USERNAME = "dido@dido.com";
    public static final String PASSWORD = "dido";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void exampleTest() {
        HttpEntity<String> entity = new HttpEntity<>(createBookingJson(), createHeaders(USERNAME, PASSWORD));
        ResponseEntity<String> body = this.restTemplate.exchange("/offers/1/bookings", HttpMethod.POST, entity, String.class);
        assertThat(body.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
            setContentType(MediaType.APPLICATION_JSON);
        }};
    }

    private String createBookingJson() {
        return "{\"fromDate\": \"2017-04-23T11:00:00.511Z\", \"toDate\": \"2017-04-28T11:00:00.511Z\"}";
    }
}
