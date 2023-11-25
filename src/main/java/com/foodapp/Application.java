package com.foodapp;

import com.foodapp.controller.DeliveryAgentController;
import com.foodapp.controller.LoginController;
import com.foodapp.model.User;
import com.foodapp.view.CustomerView;
import com.foodapp.view.DeliveryAgentView;
import com.foodapp.view.LoginView;
import com.foodapp.view.RestaurantView;

import java.util.Objects;

public class Application {

    private static Application instance;   // Singleton pattern

    private String FOOD_APP_URL = "http://localhost:8000";

    private User user;

    private LoginView loginView;
    private LoginController loginController;
    private CustomerView customerView;
    private RestaurantView restaurantView;
    private DeliveryAgentView deliveryAgentView;
    private DeliveryAgentController deliveryAgentController;

    public Application() {
        this.loginController = new LoginController(FOOD_APP_URL);
        this.loginView = new LoginView(loginController);
        this.customerView = new CustomerView();
        this.restaurantView = new RestaurantView();
        this.deliveryAgentController = new DeliveryAgentController(FOOD_APP_URL);
        this.deliveryAgentView = new DeliveryAgentView(deliveryAgentController);
    }

    public static Application getInstance() {
        if (Objects.isNull(instance)) {
            instance = new Application();
        }
        return instance;
    }

    public LoginView getLoginView() {
        return loginView;
    }

    public void setCurrentUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public CustomerView getCustomerView() {
        return customerView;
    }

    public RestaurantView getRestaurantView() {
        return restaurantView;
    }

    public DeliveryAgentView getDeliveryAgentView() {
        return deliveryAgentView;
    }

    public static void main(String[] args) {
        Application.getInstance().getLoginView().setVisible(true);
    }

}
