package com.foodapp.view;
import com.foodapp.model.Order;
import com.foodapp.model.User;
import com.foodapp.util.SpringUtilities;

import com.foodapp.Application;
import com.foodapp.controller.DishController;
import com.foodapp.model.Dish;
import com.foodapp.model.restuarant;
import com.foodapp.util.SpringUtilities;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.List;

public class DishView extends JFrame{
    private DishController dishController;
    private JPanel dishsPanel;
    private List<Dish> dish;
    private int[] counts;
    private JLabel[] countLabels;
    public DishView(DishController dishController){
        this.dishController = dishController;
        this.setupLayout();
    }

    private void setupLayout() {
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        JLabel title = new JLabel("Dishes Page");
        title.setFont(new Font("Sans Serif", Font.BOLD, 24));
        JPanel panelTitle = new JPanel();
        panelTitle.add(title);
        this.getContentPane().add(panelTitle);

        dishsPanel = new JPanel();
        dishsPanel.setLayout(new BoxLayout(dishsPanel, BoxLayout.Y_AXIS));

        JScrollPane dishPane = new JScrollPane(dishsPanel);
        this.getContentPane().add(dishPane);
    }

    public void loadData() {
        String token = Application.getInstance().getToken();
        int id = Application.getInstance().getUserId();
        int rid = Application.getInstance().getRestaurantId();
        try {
            dish = dishController.getdish(token,id,rid);
            updateView();
        } catch (Exception e) {
            System.out.println("Could not fetch order data");
            e.printStackTrace();
        }
    }

    private void updateView(){
        this.dishsPanel.removeAll();
        List<JPanel> dishPanels = new ArrayList<>();

        int numberOfCounters = dish.size();
        counts = new int[numberOfCounters];
        countLabels = new JLabel[numberOfCounters];
        int i=0;

        for (Dish item: dish) {
            try {
                JPanel dishPanel = new JPanel();
                dishPanel.setLayout(new BoxLayout(dishPanel, BoxLayout.X_AXIS));

                int width = 200;
                int height = 100;
                String base64Image = item.getImage().split(",")[1];
                byte[] imgBytes = Base64.getDecoder().decode(base64Image);
                BufferedImage img = ImageIO.read(new ByteArrayInputStream(imgBytes));
                Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                JLabel imageLabel = new JLabel(scaledIcon);
                imageLabel.setBorder(new EmptyBorder(10, 10, 10, 10));

                JPanel detailsPanel = new JPanel();
                detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
                JLabel dishNameLabel = new JLabel(item.getName(), SwingConstants.CENTER);
                dishNameLabel.setFont(new Font("Sans Serif", Font.BOLD, 18));
                dishNameLabel.setBorder(new EmptyBorder(10,10,10,10));

                JPanel descPanel = new JPanel();
                descPanel.setLayout(new BoxLayout(descPanel, BoxLayout.X_AXIS));
                JLabel descLabel = new JLabel("Description: " + item.getDescription());
                descLabel.setBorder(new EmptyBorder(10,10,10,10));
                JLabel priceLabel = new JLabel("Price: $" + item.getPrice());
                priceLabel.setBorder(new EmptyBorder(10,10,10,10));
                descPanel.add(descLabel);
                descPanel.add(priceLabel);
                detailsPanel.add(dishNameLabel);
                detailsPanel.add(descPanel);

                JPanel dlist = new JPanel();
                countLabels[i] = new JLabel(String.valueOf(counts[i]));
                JButton incrementButton = new JButton("+");
                JButton decrementButton = new JButton("-");
                int finalI = i;

                incrementButton.addActionListener(e -> {
                    item.setQuantity(item.getQuantity()+1);
                    counts[finalI]++;
                    updateLabel(finalI);
                });

                decrementButton.addActionListener(e -> {
                    item.setQuantity(item.getQuantity()-1);
                    counts[finalI]--;
                    updateLabel(finalI);
                });

                dlist.add(countLabels[i]);
                dlist.add(incrementButton);
                dlist.add(decrementButton);
                i=i+1;

                dishPanel.add(imageLabel);
                dishPanel.add(detailsPanel);
                dishPanel.add(dlist);

                dishPanels.add(dishPanel);
            }
            catch (Exception e){
                System.out.println("Something failed. Try again.");
                e.printStackTrace();
            }
        }
        for (JPanel dishPanel: dishPanels) {
            this.dishsPanel.add(dishPanel);
        }
        this.dishsPanel.invalidate();

        JButton button = new JButton("Pay Bill");
        button.addActionListener(e -> {
            gotobill();
        });
        dishsPanel.add(button, BorderLayout.SOUTH);
    }

    private void updateLabel(int index) {
        countLabels[index].setText(String.valueOf(counts[index]));
    }

    private void gotobill(){
        String token = Application.getInstance().getToken();
        int id = Application.getInstance().getUserId();
        int rid = Application.getInstance().getRestaurantId();
        Map<String, Object> requestDataList = new HashMap<>();;
        List<Map<String, Object>> dishes = new ArrayList<>();

        requestDataList.put("restaurantId", Application.getInstance().getRestaurantId());

        for (Dish item: dish){
            if (item.getQuantity()>0){
                Map<String, Object> dishData = new HashMap<>();
                dishData.put("dishId", item.getDishId());
                dishData.put("quantity", item.getQuantity());
                dishes.add(dishData);
            }
        }
        requestDataList.put("dishes", dishes);

        try {
            dishController.createorder(token, id, rid, requestDataList);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }

        Application.getInstance().getDishView().setVisible(false);
        Application.getInstance().getBillView().setVisible(true);
    }
}
