package com.foodapp;

import com.foodapp.controller.DeliveryAgentController;
import com.foodapp.controller.DishController;
import com.foodapp.controller.LoginController;
import com.foodapp.controller.orderingController;
import com.foodapp.model.User;
import com.foodapp.view.*;

import java.util.Objects;

public class Application {

    private static Application instance;   // Singleton pattern

    private String FOOD_APP_URL = "http://localhost:8000";
//
    private User user;
    private String token;
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    private int userId;
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    private int restaurantId;
    public int getRestaurantId() { return restaurantId; }
    public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }

    private LoginView loginView;
    private LoginController loginController;
    private CustomerView customerView;
    private orderingController OrderingController;
    private DishController dishController;
    private RestaurantView restaurantView;
    private DeliveryAgentView deliveryAgentView;
    private DeliveryAgentController deliveryAgentController;
    private DishView dishView;

    public Application() {
        this.loginController = new LoginController(FOOD_APP_URL);
        this.OrderingController = new orderingController(FOOD_APP_URL);
        this.dishController = new DishController(FOOD_APP_URL);
        this.loginView = new LoginView(loginController);
        this.customerView = new CustomerView(OrderingController);
        this.restaurantView = new RestaurantView();
        this.deliveryAgentController = new DeliveryAgentController(FOOD_APP_URL);
        this.deliveryAgentView = new DeliveryAgentView(deliveryAgentController);
        this.dishView = new DishView(dishController);
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

    public DishView getDishView() { return dishView; }

    public void setDishView(DishView dishView) { this.dishView = dishView; }

    public static void main(String[] args) {
        Application.getInstance().getLoginView().setVisible(true);
    }

}
