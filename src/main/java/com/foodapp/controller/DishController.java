package com.foodapp.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodapp.model.Dish;
import com.foodapp.model.restuarant;
import com.foodapp.util.JsonUtil;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DishController {
    private JsonUtil jsonUtil;
    private String url;
    private HttpClient httpClient;
    public DishController(String url) {
        this.url = url;
        this.httpClient = HttpClient.newHttpClient();
    }

    public List<Dish> getdish(String token, int id, int rid) throws URISyntaxException, IOException, InterruptedException{
        URI targetURI = new URI(url + "/dishes?userId="+String.valueOf(id)+"&token="+token+"&restaurantId="+String.valueOf(rid));
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(targetURI).GET().build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(response.body());

            if (jsonNode.isArray()) {
                List<Dish> dish = new ArrayList<>();

                for (JsonNode objNode : jsonNode) {
                    Dish edish = objectMapper.treeToValue(objNode, Dish.class);
                    dish.add(edish);
                }
                return dish;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createorder(String token, int id, int rid, List<Map<String, Object>> dish) throws URISyntaxException, IOException, InterruptedException{
        URI targetURI = new URI(url + "/createOrder?userId="+String.valueOf(id)+"&token="+token);

        Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("restaurantId", rid);
        requestBodyMap.put("dishes", dish);

        ObjectMapper objectMapper = new ObjectMapper();
        String dishJson = objectMapper.writeValueAsString(dish);
        System.out.println(dishJson);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(targetURI)
                .POST(HttpRequest.BodyPublishers.ofString(dishJson))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        // Handle the response as needed
        if (response.statusCode() == 201) {
            System.out.println("Dish added successfully!");
        } else {
            System.err.println("Failed to add dish. Status code: " + response.statusCode());
            System.err.println("Response body: " + response.body());
        }
    }
}
