package com.foodapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodapp.model.Bill;
import com.foodapp.util.JsonUtil;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class BillController {
    private JsonUtil jsonUtil;
    private String url;
    private HttpClient httpClient;
    public BillController(String url) {
        this.url = url;
        this.httpClient = HttpClient.newHttpClient();
    }
    public void payorder(String token, int id, Bill bill) throws URISyntaxException, IOException, InterruptedException{
        URI targetURI = new URI(url + "/payOrder?userId="+String.valueOf(id)+"&token="+token);

        ObjectMapper objectMapper = new ObjectMapper();
        String billJson = objectMapper.writeValueAsString(bill);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(targetURI)
                .POST(HttpRequest.BodyPublishers.ofString(billJson))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            System.out.println("Dish added successfully!");
        }
        else {
            System.err.println("Failed to pay Bill: " + response.statusCode());
        }
    }
}
