package com.foodapp.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodapp.model.Dish;
import com.foodapp.model.Order;
import com.foodapp.util.JsonUtil;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class OrderController {
    private JsonUtil jsonUtil;
    private String url;
    private HttpClient httpClient;
    public OrderController(String url) {
        this.url = url;
        this.httpClient = HttpClient.newHttpClient();
    }

    public List<Order> getorders(String token, int id) throws URISyntaxException, IOException, InterruptedException{
        URI targetURI = new URI(url + "/customerOrders?userId="+String.valueOf(id)+"&token="+token);
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(targetURI).GET().build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        List<Order> orders = JsonUtil.fromJson(response.body(), new TypeReference<>() {});
        return orders;
    }

    public String getdish(String token, int id, int did) throws URISyntaxException, IOException, InterruptedException{
        URI targetURI = new URI(url + "/dishDetails?userId="+String.valueOf(id)+"&token="+token+"&dishId="+String.valueOf(did));
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(targetURI).GET().build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        Dish dish = JsonUtil.fromJson(response.body(), Dish.class);
        return dish.getName();
    }
}
