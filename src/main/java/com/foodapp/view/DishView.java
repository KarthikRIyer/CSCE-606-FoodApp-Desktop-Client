package com.foodapp.view;
import com.foodapp.util.SpringUtilities;

import com.foodapp.Application;
import com.foodapp.controller.DishController;
import com.foodapp.model.Dish;
import com.foodapp.model.restuarant;
import com.foodapp.util.SpringUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
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
import java.util.*;
import java.util.List;

public class DishView extends JFrame implements ActionListener{
    private DishController dishController;
    private JPanel main;
    private int[] counts;
    private JLabel[] countLabels;
    private List<Dish> dish;
    private JButton loadish = new JButton("Welcome to Dishes Page! Click to load Dishes!");
    private JButton paybill = new JButton("Go to Payment");
    public DishView(DishController dishController){
        this.dishController = dishController;
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 300);
        JLabel title = new JLabel("Customer View");
        title.setFont(new Font("Sans Serif", Font.BOLD, 24));
        JPanel panelTitle = new JPanel();
        panelTitle.add(title);
        this.getContentPane().add(panelTitle);

        main = new JPanel();
        main.add(loadish);
        main.add(paybill);
        SpringUtilities.makeCompactGrid(main,
                3, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
        this.getContentPane().add(main);
        loadish.addActionListener(this);
        paybill.addActionListener(this);
        //this.updateTable();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loadish){
            try {
                getdishes();
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
        else if(e.getSource() == paybill){
            gotobill();
        }
    }

    private void getdishes() throws URISyntaxException, IOException, InterruptedException {
        String token = Application.getInstance().getToken();
        int id = Application.getInstance().getUserId();
        int rid = Application.getInstance().getRestaurantId();
        dish = dishController.getdish(token,id,rid);
        updateTable();
    }


    private void updateTable(){
        System.out.println(dish);
        int numberOfCounters = dish.size();
        counts = new int[numberOfCounters];
        countLabels = new JLabel[numberOfCounters];
        int i=0;

        for (Dish item: dish){
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
            dlist.add(new JLabel(item.getName()));
            dlist.add(new JLabel(item.getDescription()));
            dlist.add(new JLabel(String.valueOf(item.getPrice())));
            dlist.add(countLabels[i]);
            dlist.add(incrementButton);
            dlist.add(decrementButton);
            main.add(dlist);
            i=i+1;
        }
    }

    private void updateLabel(int index) {
        countLabels[index].setText(String.valueOf(counts[index]));
    }

    private void gotobill(){
        String token = Application.getInstance().getToken();
        int id = Application.getInstance().getUserId();
        int rid = Application.getInstance().getRestaurantId();
        List<Map<String, Object>> requestDataList = new ArrayList<>();

        for (Dish item: dish){
            if (item.getQuantity()>0){
                Map<String, Object> dishData = new HashMap<>();
                dishData.put("dishId", item.getDishId());
                dishData.put("quantity", item.getQuantity());
                requestDataList.add(dishData);
            }
        }

        try {
            dishController.createorder(token, id, rid, requestDataList);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
    }
}
