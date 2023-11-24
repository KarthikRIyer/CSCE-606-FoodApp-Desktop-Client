package com.foodapp.controller;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.foodapp.model.User;
import com.foodapp.model.restuarant;
import com.foodapp.util.JsonUtil;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.lang.reflect.Type;

public class orderingController {
    private JsonUtil jsonUtil;
    private String url;
    private HttpClient httpClient;
    public orderingController(String url) {
        this.url = url;
        this.httpClient = HttpClient.newHttpClient();
    }
    public List<restuarant> getbyname(String token, int uid, String name) throws URISyntaxException, IOException, InterruptedException {
        String ename = URLEncoder.encode(name, StandardCharsets.UTF_8);
        URI targetURI = new URI(url + "/searchRestaurantByName?userId="+String.valueOf(uid)+"&token="+token+"&name="+ename);
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(targetURI).GET().build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(response.body());

            if (jsonNode.isArray()) {
                List<restuarant> res = new ArrayList<>();

                for (JsonNode objNode : jsonNode) {
                    // Deserialize each object in the array
                    restuarant eres = objectMapper.treeToValue(objNode, restuarant.class);
                    res.add(eres);
                }
                return res;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<restuarant> getbycuisine(String token, int uid, String cui) throws URISyntaxException, IOException, InterruptedException {
        String ecui = URLEncoder.encode(cui, StandardCharsets.UTF_8);
        URI targetURI = new URI(url + "/searchRestaurantByCuisine?userId="+String.valueOf(uid)+"&token="+token+"&cuisine="+ecui);
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(targetURI).GET().build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(response.body());

            if (jsonNode.isArray()) {
                List<restuarant> res = new ArrayList<>();

                for (JsonNode objNode : jsonNode) {
                    // Deserialize each object in the array
                    restuarant eres = objectMapper.treeToValue(objNode, restuarant.class);
                    res.add(eres);
                }
                return res;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<restuarant> getbyrating(String token, int uid, String rat) throws URISyntaxException, IOException, InterruptedException {
        int rating = rat.charAt(0)-48;
        URI targetURI = new URI(url + "/searchRestaurantByRating?userId="+String.valueOf(uid)+"&token="+token+"&rating="+String.valueOf(rating));
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(targetURI).GET().build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(response.body());

            if (jsonNode.isArray()) {
                List<restuarant> res = new ArrayList<>();
                String img = null;
                for (JsonNode objNode : jsonNode) {
                    restuarant eres = objectMapper.treeToValue(objNode, restuarant.class);
                    img = getimage(uid,token,eres.getRestaurantId());
                    eres.setImage(img);
                    res.add(eres);
                }
                return res;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getimage(int id, String token, int rid) throws URISyntaxException, IOException, InterruptedException {
        URI targetURI = new URI(url + "/restaurantImage?userId="+String.valueOf(id)+"&token="+token+"&restaurantId="+String.valueOf(rid));
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(targetURI).GET().build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

}
