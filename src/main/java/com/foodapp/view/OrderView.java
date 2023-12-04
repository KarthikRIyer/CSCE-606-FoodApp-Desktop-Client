package com.foodapp.view;

import com.foodapp.Application;
import com.foodapp.controller.DishController;
import com.foodapp.controller.OrderController;
import com.foodapp.model.Order;
import com.foodapp.model.OrderItem;
import com.foodapp.model.enums.OrderStatus;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

public class OrderView extends JFrame {
    private OrderController orderController;
    private JPanel orderPanel;
    private List<Order> orders;
    private JProgressBar progressBar;
    private int progressValue;
    private Map<Integer, String> nameMap = new HashMap<>();

    public OrderView(OrderController orderController){
        this.orderController = orderController;
        this.setupLayout();
    }

    private void setupLayout(){
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        JLabel title = new JLabel("Customer Order View Page");
        title.setFont(new Font("Sans Serif", Font.BOLD, 24));
        JPanel panelTitle = new JPanel();
        panelTitle.add(title);
        this.getContentPane().add(panelTitle);

        orderPanel = new JPanel();
        orderPanel.setLayout(new BoxLayout(orderPanel, BoxLayout.Y_AXIS));

        JButton button = new JButton("Return to Home");
        button.addActionListener(e -> {
            Application.getInstance().getOrderView().setVisible(false);
            Application.getInstance().getCustomerView().setVisible(true);
        });
        this.getContentPane().add(button);

        JScrollPane orderPane = new JScrollPane(orderPanel);
        this.getContentPane().add(orderPane);
    }

    public void loadData(){
        String token = Application.getInstance().getToken();
        int id = Application.getInstance().getUserId();
        try {
            orders = orderController.getorders(token,id);
            updateView();
        } catch (Exception e) {
            System.out.println("Could not fetch order data");
            e.printStackTrace();
        }
    }

    private void updateView(){
        this.orderPanel.removeAll();


        //orderPanel.add(button);

        List <JPanel> orderPanels = new ArrayList<>();

        for(Order item: orders){
            try {
                JPanel oPanel = new JPanel();
                oPanel.setLayout(new BoxLayout(oPanel, BoxLayout.X_AXIS));

                JPanel idPanel = new JPanel();
                idPanel.setLayout(new BoxLayout(idPanel, BoxLayout.X_AXIS));
                JLabel idLabel = new JLabel(String.valueOf(item.getOrderId()));
                idLabel.setBorder(new EmptyBorder(10,10,10,10));
                idPanel.add(idLabel);

                JPanel progressPanel = new JPanel();
                progressBar = new JProgressBar();
                progressBar.setStringPainted(true);
                progressValue = percent(item.getOrderStatus());
                progressBar.setValue(progressValue);
                progressPanel.add(progressBar);

                JPanel listPanel = new JPanel();
                for (OrderItem items: item.getOrderItems()){
                    String name = dishName(items.getDishId()) + " - " +String.valueOf(items.getQuantity());
                    listPanel.add(new JLabel(name));
                }

                oPanel.add(idPanel);
                oPanel.add(progressPanel);
                oPanel.add(listPanel);

                orderPanels.add(oPanel);
            }
            catch (Exception e){
                System.out.println("Something failed. Try again.");
                e.printStackTrace();
            }

            for (JPanel panel:orderPanels){
                this.orderPanel.add(panel);
            }
            this.orderPanel.invalidate();
        }
    }

    private int percent(OrderStatus stat){
        if (stat.equals(OrderStatus.PENDING)){
            return 20;
        }
        else if(stat.equals(OrderStatus.CONFIRMED)){
            return 40;
        }
        else if(stat.equals(OrderStatus.READY)){
            return 60;
        }
        else if(stat.equals(OrderStatus.PICKED_UP)){
            return 80;
        }
        else if(stat.equals(OrderStatus.DELIVERED)){
            return 100;
        }
        return 0;
    }

    private String dishName(int id){
        String token = Application.getInstance().getToken();
        int uid = Application.getInstance().getUserId();
        if (!nameMap.containsKey(id)) {
            String name;
            try {
                name = orderController.getdish(token,uid,id);
                nameMap.put(id,name);
                return name;
            }
            catch (Exception e) {
                System.out.println("Could not fetch");
                e.printStackTrace();
            }
        }
        else{
            return nameMap.get(id);
        }
        return null;
    }
}
