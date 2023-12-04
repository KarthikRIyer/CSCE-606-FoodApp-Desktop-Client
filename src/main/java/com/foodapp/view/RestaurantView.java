package com.foodapp.view;

import com.foodapp.Application;
import com.foodapp.controller.DishController;
import com.foodapp.controller.RestaurantController;
import com.foodapp.model.Dish;
import com.foodapp.model.DishPost;
import com.foodapp.model.Order;
import com.foodapp.model.OrderItem;
import com.foodapp.model.enums.OrderStatus;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.*;
import java.util.List;

public class RestaurantView extends JFrame {
    private static RestaurantController restaurantController;
    private JPanel dpanel;
    private JPanel opanel;
    private List<Dish> dlist;
    private List<Order> olist;
    private Map<Integer, String> nameMap = new HashMap<>();
    public static JTextField name = new JTextField(20);
    public JTextField dishDesc = new JTextField(20);
    public JTextField dishPrice = new JTextField(20);


    public RestaurantView(RestaurantController restaurantController){
        this.restaurantController = restaurantController;
        this.setupLayout();
    }

    private void setupLayout() {
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 800);
        JLabel title = new JLabel("Restaurant Portal");
        title.setFont(new Font("Sans Serif", Font.BOLD, 24));
        JPanel panelTitle = new JPanel();
        panelTitle.add(title);
        this.getContentPane().add(panelTitle);

        JFrame frame = new JFrame("Image Input Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JButton openInputButton = new JButton("Open Input Screen");
        openInputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openInputScreen(frame);
            }
        });

        dpanel = new JPanel();
        opanel = new JPanel();
        dpanel.setLayout(new BoxLayout(dpanel, BoxLayout.Y_AXIS));
        opanel.setLayout(new BoxLayout(opanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane1 = new JScrollPane(dpanel);
        JScrollPane scrollPane2 = new JScrollPane(opanel);

        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(scrollPane1);
        panel.add(scrollPane2);

        this.getContentPane().add(openInputButton);
        this.getContentPane().add(panel);
    }

    private static void openInputScreen(JFrame parentFrame) {
        JDialog inputDialog = new JDialog(parentFrame, "Input Screen", true);
        inputDialog.setSize(300, 200);

        JPanel inputPanel = new JPanel();
        JTextField imageTextField = new JTextField(20);
        JTextField field1TextField = new JTextField(20);
        JTextField field2TextField = new JTextField(20);
        JTextField field3TextField = new JTextField(20);
        JButton uploadButton = new JButton("Upload Image");
        JButton submitButton = new JButton("Submit");

        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "gif");
                fileChooser.setFileFilter(filter);

                int result = fileChooser.showOpenDialog(inputDialog);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        byte[] fileBytes = Files.readAllBytes(selectedFile.toPath());
                        String base64Encoded = Base64.getEncoder().encodeToString(fileBytes);
                        imageTextField.setText(base64Encoded);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String imageBase64 = imageTextField.getText();
                String field1 = field1TextField.getText();
                String field2 = field2TextField.getText();
                String field3 = field3TextField.getText();

                // Use the input data in another method
                processData(imageBase64, field1, field2, field3);

                // Close the dialog after processing
                inputDialog.dispose();
            }
        });

        //inputPanel.add(new JLabel("Base64 Encoded Image: "));
        //inputPanel.add(imageTextField);
        inputPanel.add(uploadButton);

        inputPanel.add(new JLabel("Dish Name"));
        inputPanel.add(field1TextField);

        inputPanel.add(new JLabel("Dish Description"));
        inputPanel.add(field2TextField);

        inputPanel.add(new JLabel("Dish Price"));
        inputPanel.add(field3TextField);

        inputPanel.add(submitButton);

        inputDialog.add(inputPanel);
        inputDialog.setVisible(true);
    }

    private static void processData(String imageBase64, String field1, String field2, String field3) {
        DishPost post = new DishPost();
        // Process or use the input data as needed in another method
        String img = "data:image/jpeg;base64," + imageBase64;
        post.setDishName(field1);
        post.setDishDesc(field2);
        post.setDishPrice(Double.parseDouble(field3));
        post.setRestaurantId(Application.getInstance().getUserId());
        post.setImage(img);
        System.out.println("Name : "+field1);

        String token = Application.getInstance().getToken();
        int id = Application.getInstance().getUserId();

        try {
            restaurantController.postdish(token,id,post);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
    }

    public void loadData() {
        String token = Application.getInstance().getToken();
        int id = Application.getInstance().getUserId();
        try {
            dlist = restaurantController.getdishes(token,id);
            updateView();
        } catch (Exception e) {
            System.out.println("Could not fetch order data");
            e.printStackTrace();
        }
    }

    private void updateView(){
        this.dpanel.removeAll();

        List<JPanel> dpanels = new ArrayList<>();

        for(Dish item: dlist){
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

                dishPanel.add(imageLabel);
                dishPanel.add(detailsPanel);

                dpanels.add(dishPanel);
            }
            catch (Exception e){
                System.out.println("Something failed. Try again.");
                e.printStackTrace();
            }
        }

        for (JPanel dishPanel: dpanels) {
            this.dpanel.add(dishPanel);
        }
        this.dpanel.invalidate();
    }

    public void loadorder(){
        String token = Application.getInstance().getToken();
        int id = Application.getInstance().getUserId();
        try {
            olist = restaurantController.getorders(token,id);
            updateOrder();
        } catch (Exception e) {
            System.out.println("Could not fetch order data");
            e.printStackTrace();
        }
    }

    private void updateOrder(){
        this.opanel.removeAll();
        List<JPanel> opanels = new ArrayList<>();

        for(Order item: olist){
            if (item.getOrderStatus() == OrderStatus.CONFIRMED){
                JPanel orderPanel = new JPanel();
                orderPanel.setLayout(new BoxLayout(orderPanel, BoxLayout.X_AXIS));

                JPanel idPanel = new JPanel();
                idPanel.setLayout(new BoxLayout(idPanel, BoxLayout.X_AXIS));
                String id = "Order " + String.valueOf(item.getOrderId());
                JLabel idLabel = new JLabel(id);
                idLabel.setBorder(new EmptyBorder(10,10,10,10));
                idPanel.add(idLabel);

                JButton popupButton = new JButton("Order List");
                String o = "";
                for(OrderItem items: item.getOrderItems()){
                    o = o + " " + dishName(items.getDishId()) + " - " + String.valueOf(items.getQuantity()) + "\n";
                }
                String finalO = o;
                popupButton.addActionListener(e -> showMessage(finalO));

                String token = Application.getInstance().getToken();
                int rid = Application.getInstance().getUserId();
                int oid = item.getOrderId();

                JButton orderButton = new JButton("Order Prepared");
                orderButton.addActionListener(e -> {
                    try {
                        restaurantController.orderPrepared(token,rid,oid);
                        orderButton.setEnabled(false);
                    } catch (Exception ei) {
                        System.out.println("Could not fetch order data");
                        ei.printStackTrace();
                    }
                });

                orderPanel.add(idPanel);
                orderPanel.add(popupButton);
                orderPanel.add(orderButton);

                opanels.add(orderPanel);
            }
        }

        for (JPanel pan: opanels) {
            this.opanel.add(pan);
        }
        this.opanel.invalidate();
    }

    private static void showMessage(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    private String dishName(int id){
        String token = Application.getInstance().getToken();
        int uid = Application.getInstance().getUserId();
        if (!nameMap.containsKey(id)) {
            String name;
            try {
                String n = restaurantController.getdish(token,uid,id);
                nameMap.put(id,n);
                return n;
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
