package com.foodapp;

import com.foodapp.controller.*;
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
    private int orderId;
    private double totalCost;
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    private LoginView loginView;
    private LoginController loginController;
    private CustomerView customerView;
    private orderingController OrderingController;
    private DishController dishController;
    private DeliveryAgentView deliveryAgentView;
    private DeliveryAgentController deliveryAgentController;
    private OrderController orderController;
    private DishView dishView;
    private BillController billController;
    private BillView billView;
    private OrderView orderView;
    private RestaurantController restaurantController;
    private RestaurantView restaurantView;

    public Application() {
        this.loginController = new LoginController(FOOD_APP_URL);
        this.OrderingController = new orderingController(FOOD_APP_URL);
        this.dishController = new DishController(FOOD_APP_URL);
        this.billController = new BillController(FOOD_APP_URL);
        this.orderController = new OrderController(FOOD_APP_URL);
        this.restaurantController = new RestaurantController(FOOD_APP_URL);
        this.loginView = new LoginView(loginController);
        this.customerView = new CustomerView(OrderingController);
        this.restaurantView = new RestaurantView(restaurantController);
        this.deliveryAgentController = new DeliveryAgentController(FOOD_APP_URL);
        this.deliveryAgentView = new DeliveryAgentView(deliveryAgentController);
        this.dishView = new DishView(dishController);
        this.billView = new BillView(billController);
        this.orderView = new OrderView(orderController);
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

    public RestaurantView getRestaurantView() { return restaurantView; }

    public DeliveryAgentView getDeliveryAgentView() {
        return deliveryAgentView;
    }

    public DishView getDishView() { return dishView; }

    public BillView getBillView() { return billView; }

    public OrderView getOrderView() { return orderView; }

    public static void main(String[] args) {
        Application.getInstance().getLoginView().setVisible(true);
    }

}
