package com.foodapp.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.foodapp.model.Order;
import com.foodapp.model.Restaurant;
import com.foodapp.model.User;
import com.foodapp.util.JsonUtil;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class DeliveryAgentController {
    private String url;
    private HttpClient httpClient;

    public DeliveryAgentController(String url) {
        this.url = url;
        this.httpClient = HttpClient.newHttpClient();
    }

    public List<Order> getReadyOrders(String userId, String token) throws URISyntaxException, IOException, InterruptedException {
        URI targetURI = new URI(url + "/readyOrders?userId="+userId+"&token="+token);
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(targetURI).GET().build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        List<Order> orders = JsonUtil.fromJson(response.body(), new TypeReference<>() {});
        return orders;
    }

    public Restaurant getRestaurantDetails(Integer restaurantId, Integer userId, String token) throws URISyntaxException, IOException, InterruptedException {
        URI targetURI = new URI(url + "/searchRestaurantById?userId="+userId+"&token="+token+"&restaurantId="+restaurantId);
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(targetURI).GET().build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        Restaurant restaurant = JsonUtil.fromJson(response.body(), Restaurant.class);
        return restaurant;
    }

    public void orderPicked(Integer userId, String token, Integer orderId) throws IOException, InterruptedException, URISyntaxException {
        URI targetURI = new URI(url + "/orderPicked?userId="+userId+"&token="+token+"&orderId="+orderId);
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(targetURI).POST(HttpRequest.BodyPublishers.noBody()).build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("Unable to update order status!");
        }
    }

    public void orderDelivered(Integer userId, String token, Integer orderId) throws IOException, InterruptedException, URISyntaxException {
        URI targetURI = new URI(url + "/orderDelivered?userId="+userId+"&token="+token+"&orderId="+orderId);
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(targetURI).POST(HttpRequest.BodyPublishers.noBody()).build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("Unable to update order status!");
        }
    }
}
