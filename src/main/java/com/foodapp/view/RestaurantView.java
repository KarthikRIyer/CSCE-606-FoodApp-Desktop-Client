package com.foodapp.view;

import javax.swing.*;
import java.awt.*;

public class RestaurantView extends JFrame {

    public RestaurantView() {
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 300);
        JLabel title = new JLabel("Restaurant View");
        title.setFont(new Font("Sans Serif", Font.BOLD, 24));
        JPanel panelTitle = new JPanel();
        panelTitle.add(title);
        this.getContentPane().add(panelTitle);
    }

}
