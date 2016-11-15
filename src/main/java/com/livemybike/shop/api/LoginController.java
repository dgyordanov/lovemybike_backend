package com.livemybike.shop.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/login")
public class LoginController {


    private static final Map<String, String> okResponseBody = new HashMap<>();

    static {
        okResponseBody.put("status", "ok");
    }

    @RequestMapping(value = "", method = GET, produces = APPLICATION_JSON_VALUE)
    public Map<String, String> login() {
        return okResponseBody;
    }
}
