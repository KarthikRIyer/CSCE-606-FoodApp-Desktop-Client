package com.foodapp.controller;

import com.foodapp.model.User;
import com.foodapp.util.JsonUtil;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LoginController {

    private String url;
    private HttpClient httpClient;

    public LoginController(String url) {
        this.url = url;
        this.httpClient = HttpClient.newHttpClient();
    }

    public User login(String userType, String username, String password) throws URISyntaxException, IOException, InterruptedException {
        URI targetURI = new URI(url + "/login?type="+userType+"&username="+username+"&password="+password);
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(targetURI).GET().build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        User user = JsonUtil.fromJson(response.body(), User.class);
        return user;
    }

}
