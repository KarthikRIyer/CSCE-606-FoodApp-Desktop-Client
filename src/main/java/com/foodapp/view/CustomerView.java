package com.foodapp.view;

import com.foodapp.model.Order;
import com.foodapp.model.Restaurant;
import com.foodapp.util.SpringUtilities;
import com.foodapp.Application;
import com.foodapp.controller.orderingController;
import com.foodapp.model.restuarant;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class CustomerView extends JFrame implements ActionListener {
    private JTextField searchField = new JTextField(10);;
    private JButton searchButton = new JButton("Search");
    public JTextField getSearchField() {return searchField;}
    public JButton getSearchButton() {return searchButton;}
    private String cuisine[] = {"Indian", "Fast Food", "Italian"};
    private String rating[] = {"1 Star", "2 Star", "3 Star", "4 Star", "5 Star"};
    private JComboBox<String> cuisineBox = new JComboBox<>(cuisine);
    private JComboBox<String> ratingBox = new JComboBox<>(rating);
    private JTable restaurantTable;
    private DefaultTableModel tableModel;
    private orderingController OrderingController;
    private JPanel main;
    private JPanel view;
    private DefaultTableModel items;
    private JTable tblItems;
    public CustomerView(orderingController OrderingController) {
        this.OrderingController = OrderingController;

        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 300);
        JLabel title = new JLabel("Customer View");
        title.setFont(new Font("Sans Serif", Font.BOLD, 24));
        JPanel panelTitle = new JPanel();
        panelTitle.add(title);
        this.getContentPane().add(panelTitle);

        main = new JPanel();
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        main.add(new JLabel("Search by Name"));
        main.add(searchField);
        main.add(searchButton);


        ratingBox.setSelectedItem(rating[0]);
        cuisineBox.setSelectedItem(cuisine[0]);
        main.add(ratingBox);
        main.add(cuisineBox);
        //main.add(tableScrollPane);

        SpringUtilities.makeCompactGrid(main,
                3, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad

        this.getContentPane().add(main);
        view = new JPanel();
        view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));
        JScrollPane orderPane = new JScrollPane(view);
        this.getContentPane().add(view);
        searchButton.addActionListener(this);
        ratingBox.addActionListener(this);
        cuisineBox.addActionListener(this);
    }

    public JComboBox<String> getRatingBox() {
        return ratingBox;
    }
    public JComboBox<String> getCuisineBox() {
        return cuisineBox;
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton){
            try {
                searchResName();
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
        else if (e.getSource() == ratingBox){
            try {
                searchResRat();
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
        else if (e.getSource() == cuisineBox){
            try {
                searchResCui();
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void searchResName() throws URISyntaxException, IOException, InterruptedException {
        System.out.println(searchField.getText());
        String token = Application.getInstance().getToken();
        int id = Application.getInstance().getUserId();
        List<restuarant> res = OrderingController.getbyname(token,id,searchField.getText());
        updateTable(res);
    }

    private void searchResRat() throws URISyntaxException, IOException, InterruptedException {
        String rat = (String) getRatingBox().getSelectedItem();
        String token = Application.getInstance().getToken();
        int id = Application.getInstance().getUserId();
        List<restuarant> res = OrderingController.getbyrating(token,id,rat);
        updateTable(res);
    }

    private void searchResCui() throws URISyntaxException, IOException, InterruptedException {
        String cui = (String) getCuisineBox().getSelectedItem();
        String token = Application.getInstance().getToken();
        int id = Application.getInstance().getUserId();
        List<restuarant> res = OrderingController.getbycuisine(token,id,cui);
        updateTable(res);
    }

    private void updateTable(List<restuarant> res) {
        this.view.removeAll();
        List<JPanel> orderPanels = new ArrayList<>();
        for (restuarant item: res) {
            try {
                JPanel orderPanel = new JPanel();
                orderPanel.setLayout(new BoxLayout(orderPanel, BoxLayout.X_AXIS));

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
                JLabel linkLabel = new JLabel();
                linkLabel.setText("<html><a href='#'>" + item.getName() + "</a></html>");
                linkLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                linkLabel.setForeground(Color.BLUE);
                linkLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        Application.getInstance().setRestaurantId(item.getRestaurantId());
                        Application.getInstance().getCustomerView().setVisible(false);
                        System.out.println(Application.getInstance().getRestaurantId());
                        Application.getInstance().getDishView().setVisible(true);
                        Application.getInstance().getDishView().loadData();
                    }
                });

                JPanel descPanel = new JPanel();
                descPanel.setLayout(new BoxLayout(descPanel, BoxLayout.X_AXIS));
                JLabel descLabel = new JLabel(item.getDesc());
                descLabel.setBorder(new EmptyBorder(10,10,10,10));
                JLabel addrLabel = new JLabel("Address: " + item.getAddress());
                addrLabel.setBorder(new EmptyBorder(10,10,10,10));
                descPanel.add(descLabel);
                descPanel.add(addrLabel);
                detailsPanel.add(linkLabel);
                detailsPanel.add(descPanel);

                JPanel ratingPanel = new JPanel();
                ratingPanel.setLayout(new BoxLayout(ratingPanel, BoxLayout.X_AXIS));
                JLabel ratingLabel = new JLabel(String.valueOf(item.getRating()));
                ratingLabel.setBorder(new EmptyBorder(10,10,10,10));
                ratingPanel.add(ratingLabel);

                orderPanel.add(imageLabel);
                orderPanel.add(detailsPanel);
                orderPanel.add(ratingPanel);

                orderPanels.add(orderPanel);
            }
            catch (Exception e){
                System.out.println("Something failed. Try again.");
                e.printStackTrace();
            }
        }
        for (JPanel orderPanel: orderPanels) {
            this.view.add(orderPanel);
        }
        this.view.invalidate();
    }

}
