package com.foodapp.view;

import com.foodapp.Application;
import com.foodapp.controller.DeliveryAgentController;
import com.foodapp.model.Order;
import com.foodapp.model.Restaurant;
import com.foodapp.model.User;
import com.foodapp.model.enums.OrderStatus;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Logger;

public class DeliveryAgentView extends JFrame {

    private Logger logger = Logger.getLogger(DeliveryAgentView.class.getName());
    private DeliveryAgentController deliveryAgentController;
    private JPanel ordersPanel;

    public DeliveryAgentView(DeliveryAgentController deliveryAgentController) {
        this.deliveryAgentController = deliveryAgentController;
        this.setupLayout();
    }

    private void setupLayout() {
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        JLabel title = new JLabel("Delivery Agent");
        title.setFont(new Font("Sans Serif", Font.BOLD, 24));
        JPanel panelTitle = new JPanel();
        panelTitle.add(title);
        this.getContentPane().add(panelTitle);

        ordersPanel = new JPanel();
        ordersPanel.setLayout(new BoxLayout(ordersPanel, BoxLayout.Y_AXIS));
//        for (int i = 0; i < 10; i++) {
//            JLabel x = new JLabel(""+i) {
//                @Override
//                public Dimension getMaximumSize() {
//                    Dimension d = super.getMaximumSize();
//                    d.width = Integer.MAX_VALUE;
//                    return d;
//                }
//            };
//            x.setHorizontalAlignment(JLabel.CENTER);
//            x.addMouseListener(new MouseListener() {
//                @Override
//                public void mouseClicked(MouseEvent e) {
//                    System.out.println(x.getText() + " clicked");
//                }
//
//                @Override
//                public void mousePressed(MouseEvent e) {
//
//                }
//
//                @Override
//                public void mouseReleased(MouseEvent e) {
//
//                }
//
//                @Override
//                public void mouseEntered(MouseEvent e) {
//
//                }
//
//                @Override
//                public void mouseExited(MouseEvent e) {
//
//                }
//            });
//            orderPanel.add(x);
//        }
        JScrollPane orderPane = new JScrollPane(ordersPanel);
        this.getContentPane().add(orderPane);
    }

    public void loadData() {
        User user = Application.getInstance().getUser();
        try {
            List<Order> orders = deliveryAgentController.getReadyOrders(user.getUserId().toString(), user.getToken());
            updateView(orders);
        } catch (Exception e) {
            logger.severe("Could not fetch order data");
            e.printStackTrace();
        }
    }

    private void updateView(List<Order> orders) {
        User user = Application.getInstance().getUser();
        this.ordersPanel.removeAll();
        List<JPanel> orderPanels = new ArrayList<>();
        for (Order order: orders) {
            try {
                Restaurant restaurant = deliveryAgentController.getRestaurantDetails(order.getRestaurantId(), user.getUserId(), user.getToken());
                String address = order.getAddress();

                JPanel orderPanel = new JPanel();
                orderPanel.setLayout(new BoxLayout(orderPanel, BoxLayout.X_AXIS));

                String base64Image = restaurant.getImage().split(",")[1];
                byte[] imgBytes = Base64.getDecoder().decode(base64Image);
                BufferedImage img = ImageIO.read(new ByteArrayInputStream(imgBytes));
                JLabel imageLabel = new JLabel(new ImageIcon(img));
                imageLabel.setBorder(new EmptyBorder(10,10,10,10));

                JPanel detailsPanel = new JPanel();
                detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
                JLabel restaurantNameLabel = new JLabel(restaurant.getName(), SwingConstants.CENTER);
                restaurantNameLabel.setFont(new Font("Sans Serif", Font.BOLD, 18));
                restaurantNameLabel.setBorder(new EmptyBorder(10,10,10,10));
//                restaurantNameLabel.setHorizontalAlignment(JLabel.CENTER);
                JPanel locationPanel = new JPanel();
                locationPanel.setLayout(new BoxLayout(locationPanel, BoxLayout.X_AXIS));
                JLabel restLocLabel = new JLabel("Restaurant Address: " + restaurant.getAddress());
                restLocLabel.setBorder(new EmptyBorder(10,10,10,10));
                JLabel destLocLabel = new JLabel("Delivery Address: " + order.getAddress());
                destLocLabel.setBorder(new EmptyBorder(10,10,10,10));
                locationPanel.add(restLocLabel);
                locationPanel.add(destLocLabel);
                detailsPanel.add(restaurantNameLabel);
                detailsPanel.add(locationPanel);

                String deliverButtonText = "";
                if (order.getOrderStatus().equals(OrderStatus.READY))
                    deliverButtonText = "Deliver this";
                else if (order.getOrderStatus().equals(OrderStatus.DELIVERED))
                    deliverButtonText = "Delivered";
                JButton deliverButton = new JButton(deliverButtonText);
                if (order.getOrderStatus().equals(OrderStatus.DELIVERED)) deliverButton.setEnabled(false);
                deliverButton.addActionListener(e -> {
                    if (deliverButton.getText().equals("Deliver this")) {
                        try {
                            deliveryAgentController.orderPicked(user.getUserId(), user.getToken(), order.getOrderId());
                        } catch (Exception exception) {
                            throw new RuntimeException(exception);
                        }
                        deliverButton.setText("Delivered");
                    } else if (deliverButton.getText().equals("Delivered")) {
                        try {
                            deliveryAgentController.orderDelivered(user.getUserId(), user.getToken(), order.getOrderId());
                        } catch (Exception exception) {
                            throw new RuntimeException(exception);
                        }
                        deliverButton.setEnabled(false);
                    }
                });
//                deliverButton.setBorder(new EmptyBorder(10,10,10,10));
                orderPanel.add(imageLabel);
                orderPanel.add(detailsPanel);
                orderPanel.add(deliverButton);

                orderPanels.add(orderPanel);
            } catch (Exception e){
                logger.severe("Something failed. Try again.");
                e.printStackTrace();
            }

        }

        for (JPanel orderPanel: orderPanels) {
            this.ordersPanel.add(orderPanel);
        }
        this.ordersPanel.invalidate();
    }

}
