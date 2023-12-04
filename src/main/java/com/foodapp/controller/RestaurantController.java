package com.foodapp.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodapp.Application;
import com.foodapp.model.Dish;
import com.foodapp.model.DishPost;
import com.foodapp.model.Order;
import com.foodapp.util.JsonUtil;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class RestaurantController {
    private JsonUtil jsonUtil;
    private String url;
    private HttpClient httpClient;
    public RestaurantController(String url) {
        this.url = url;
        this.httpClient = HttpClient.newHttpClient();
    }

    public List<Dish> getdishes(String token, int id) throws URISyntaxException, IOException, InterruptedException {
        URI targetURI = new URI(url + "/dishes?userId="+String.valueOf(id)+"&token="+token+"&restaurantId="+String.valueOf(id));
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

    private String getimage(int id, String token, int did) throws URISyntaxException, IOException, InterruptedException{
        URI targetURI = new URI(url + "/dishImage?userId="+String.valueOf(id)+"&token="+token+"&dishId="+String.valueOf(did));
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(targetURI).GET().build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public List<Order> getorders(String token, int id) throws URISyntaxException, IOException, InterruptedException{
        URI targetURI = new URI(url + "/restaurantOrders?restaurantId="+String.valueOf(id)+"&token="+token);
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

    public void postdish(String token, int id, DishPost dish) throws URISyntaxException, IOException, InterruptedException {
        URI targetURI = new URI(url + "/createDish?userId="+String.valueOf(id)+"&token="+token);

        ObjectMapper objectMapper = new ObjectMapper();
        String billJson = objectMapper.writeValueAsString(dish);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(targetURI)
                .POST(HttpRequest.BodyPublishers.ofString(billJson))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            System.out.println("Dish added successfully!");
        }
        else {
            System.err.println("Failed to Add: " + response.statusCode());
        }
        Application.getInstance().getRestaurantView().loadData();
        Application.getInstance().getRestaurantView().loadorder();
    }

    public void orderPrepared(String token, int id, int oid) throws URISyntaxException, IOException, InterruptedException {
        URI targetURI = new URI(url + "/orderPrepared?restaurantId="+String.valueOf(id)+"&token="+token+"&orderId="+String.valueOf(oid));

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(targetURI)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            System.out.println("Order Updated Successfully");
        } else {
            System.err.println("Failed to Update: " + response.statusCode());
        }
    }
}
