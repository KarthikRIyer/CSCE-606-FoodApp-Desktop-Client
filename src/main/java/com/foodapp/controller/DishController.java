package com.foodapp.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodapp.Application;
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
                String img = null;

                for (JsonNode objNode : jsonNode) {
                    Dish edish = objectMapper.treeToValue(objNode, Dish.class);
                    img = getimage(id,token,edish.getDishId());
                    edish.setImage(img);
                    dish.add(edish);
                }
                return dish;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createorder(String token, int id, int rid, Map<String, Object> dish) throws URISyntaxException, IOException, InterruptedException{
        URI targetURI = new URI(url + "/createOrder?userId="+String.valueOf(id)+"&token="+token);

        ObjectMapper objectMapper = new ObjectMapper();
        String dishJson = objectMapper.writeValueAsString(dish);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(targetURI)
                .POST(HttpRequest.BodyPublishers.ofString(dishJson))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            System.out.println("Dish added successfully!");
            try {
                ObjectMapper Mapper = new ObjectMapper();
                JsonNode jsonNode = Mapper.readTree(response.body());

                int orderId = jsonNode.get("orderId").asInt();
                double cost = jsonNode.get("totalCost").asInt();
                System.out.println(orderId);

                Application.getInstance().setOrderId(orderId);
                Application.getInstance().setTotalCost(cost);

            } catch(Exception e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Failed to add dish. Status code: " + response.statusCode());
            System.err.println("Response body: " + response.body());
        }
    }

    private String getimage(int id, String token, int did) throws URISyntaxException, IOException, InterruptedException{
        URI targetURI = new URI(url + "/dishImage?userId="+String.valueOf(id)+"&token="+token+"&dishId="+String.valueOf(did));
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(targetURI).GET().build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
