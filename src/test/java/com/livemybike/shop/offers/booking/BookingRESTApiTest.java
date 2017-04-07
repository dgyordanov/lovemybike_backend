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

/**
 * NOTE: The offer with ID 1 should be owned by dido@dido.com!
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class BookingRESTApiTest extends AbstractBookingTest {

    private static final String USERNAME1 = "dido@dido.com";
    private static final String PASSWORD1 = "dido";
    private static final String USERNAME2 = "petyo@petyo.com";
    private static final String PASSWORD2 = "petyo";
    private static final String USERNAME3 = "viki@viki.com";
    private static final String PASSWORD3 = "viki";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void requestBooking() {
        HttpEntity<String> entity = new HttpEntity<>(createBookingJson(), createHeaders(USERNAME2, PASSWORD2));
        ResponseEntity<String> body = restTemplate.exchange("/offers/1/bookings", HttpMethod.POST, entity, String.class);
        assertThat(body.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void requestBookingNotAuthorized() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(createBookingJson(), headers);
        ResponseEntity<String> body = restTemplate.exchange("/offers/1/bookings", HttpMethod.POST, entity, String.class);
        assertThat(body.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void requestBookingInvalidDates() {
        HttpEntity<String> entity = new HttpEntity<>(createBookingJsonFromDateAfterToDate(), createHeaders(USERNAME2, PASSWORD2));
        ResponseEntity<String> body = restTemplate.exchange("/offers/1/bookings", HttpMethod.POST, entity, String.class);
        assertThat(body.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void requestBookingWithoutFromDate() {
        HttpEntity<String> entity = new HttpEntity<>("{\"toDate\": \"2017-04-28T11:00:00.511Z\"}", createHeaders(USERNAME2, PASSWORD2));
        ResponseEntity<String> body = restTemplate.exchange("/offers/1/bookings", HttpMethod.POST, entity, String.class);
        assertThat(body.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void requestBookingWithoutToDate() {
        HttpEntity<String> entity = new HttpEntity<>("{\"fromDate\": \"2017-04-23T11:00:00.511Z\"}", createHeaders(USERNAME2, PASSWORD2));
        ResponseEntity<String> body = restTemplate.exchange("/offers/1/bookings", HttpMethod.POST, entity, String.class);
        assertThat(body.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void approveBooking() {
        HttpEntity<String> requestedBookingEntity = new HttpEntity<>(createBookingJson(), createHeaders(USERNAME2, PASSWORD2));
        BookingDTO requestedBooking = restTemplate.exchange(
                "/offers/1/bookings", HttpMethod.POST, requestedBookingEntity, BookingDTO.class).getBody();

        HttpEntity<String> approveBookingEntity = new HttpEntity<>(createBookingJson(), createHeaders(USERNAME1, PASSWORD1));
        ResponseEntity<BookingDTO> response = restTemplate.exchange(String.format("//bookings/%s/@approve", requestedBooking.getId().toString())
                , HttpMethod.GET, approveBookingEntity, BookingDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void approveNotExistingBooking() {
        HttpEntity<String> approveBookingEntity = new HttpEntity<>(createBookingJson(), createHeaders(USERNAME1, PASSWORD1));
        ResponseEntity<BookingDTO> response = restTemplate.exchange("//bookings/234132/@approve"
                , HttpMethod.GET, approveBookingEntity, BookingDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void approveBookingNotMyOffer() {
        HttpEntity<String> requestedBookingEntity = new HttpEntity<>(createBookingJson(), createHeaders(USERNAME2, PASSWORD2));
        BookingDTO requestedBooking = restTemplate.exchange(
                "/offers/1/bookings", HttpMethod.POST, requestedBookingEntity, BookingDTO.class).getBody();

        HttpEntity<String> approveBookingEntity = new HttpEntity<>(createBookingJson(), createHeaders(USERNAME3, PASSWORD3));
        ResponseEntity<String> response = restTemplate.exchange(String.format("//bookings/%s/@approve", requestedBooking.getId().toString())
                , HttpMethod.GET, approveBookingEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
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

    private String createBookingJsonFromDateAfterToDate() {
        return "{\"fromDate\": \"2017-05-23T11:00:00.511Z\", \"toDate\": \"2017-04-28T11:00:00.511Z\"}";
    }
}
