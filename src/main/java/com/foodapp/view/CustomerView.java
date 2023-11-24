package com.foodapp.view;

import com.foodapp.util.SpringUtilities;
import com.foodapp.Application;
import com.foodapp.controller.orderingController;
import com.foodapp.model.restuarant;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URISyntaxException;
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

        //String[] headers = {"Image", "Description", "Rating"};
        //tableModel = new DefaultTableModel(null, headers);
        //restaurantTable = new JTable(tableModel);
        //int lastColumnIndex = restaurantTable.getColumnCount() - 1;
        //restaurantTable.getColumnModel().getColumn(lastColumnIndex).setCellRenderer(new ImageRenderer());
        //JScrollPane tableScrollPane = new JScrollPane(restaurantTable);

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
        items = new DefaultTableModel();
        tblItems = new JTable(items);
        JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        items.addColumn("ID");
        items.addColumn("Name");
        for (restuarant item: res){
            Object[] row = new Object[2];
            JLabel linkLabel = new JLabel();
            JLabel name = new JLabel();

            linkLabel.setText("<html><a href='#'>" + item.getName() + "</a></html>");

            String ltxt = "<html><a href='#'>" + item.getName() + "</a></html>";
            linkLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            linkLabel.setForeground(Color.BLUE);
            linkLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    Application.getInstance().setRestaurantId(item.getRestaurantId());
                    Application.getInstance().getCustomerView().setVisible(false);
                    System.out.println(Application.getInstance().getRestaurantId());
                    Application.getInstance().getDishView().setVisible(true);
                }
            });
            linkPanel.add(linkLabel);
            linkPanel.add(new JLabel(item.getName()));
            linkPanel.add(new JLabel(String.valueOf(item.getRating())));
            //row[0]=ltxt;
            //row[1]=item.getName();
            //items.addRow(row);
            main.add(linkPanel);
        }
        //tblItems.getColumnModel().getColumn(0).setCellRenderer(new LinkRenderer());
        //JScrollPane scrollPane = new JScrollPane(tblItems);
        //main.add(scrollPane);
    }

    class LinkRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (value != null) {
                setText(value.toString());
                setForeground(Color.BLUE);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            return this;
        }
    }

    private JLabel createLink(String text) {
        JLabel linkLabel = new JLabel("<html><u>" + text + "</u></html>");
        linkLabel.setForeground(Color.BLUE);
        linkLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return linkLabel;
    }

    private void openAnotherLayout(String restaurantName) {
        System.out.println(restaurantName);
    }

    private ImageIcon getImageIconFromBase64(String base64p) {
        try {
            String base64 = base64p.split(",")[1];
            byte[] imageBytes = Base64.getDecoder().decode(base64);
            Image image = Toolkit.getDefaultToolkit().createImage(imageBytes);
            Image scaledImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH); // Adjust the size as needed
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private class ImageRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = new JLabel();
            label.setHorizontalAlignment(JLabel.CENTER);

            if (value instanceof ImageIcon) {
                label.setIcon((ImageIcon) value);
            }

            return label;
        }
    }


}
