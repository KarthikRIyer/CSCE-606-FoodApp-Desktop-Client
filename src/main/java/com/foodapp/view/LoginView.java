package com.foodapp.view;

import com.foodapp.Application;
import com.foodapp.controller.LoginController;
import com.foodapp.model.User;
import com.foodapp.util.SpringUtilities;

import javax.swing.*;

public class LoginView extends JFrame {
    private String userTypes[] = {"CUSTOMER", "RESTAURANT", "DELIVERY"};
    private JComboBox<String> userTypeComboBox = new JComboBox<>(userTypes);
    private JTextField txtUserName = new JTextField(10);
    private JTextField txtPassword = new JTextField(10);
    private JButton    btnLogin    = new JButton("Login");
    private LoginController loginController;

    public LoginView(LoginController loginController) {
        this.loginController = loginController;
        this.setSize(300, 200);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().add(new JLabel ("EatExpress"));

        userTypeComboBox.setSelectedItem(userTypes[0]);

        JPanel main = new JPanel(new SpringLayout());
        main.add(new JLabel("User Type:"));
        main.add(userTypeComboBox);
        main.add(new JLabel("Username:"));
        main.add(txtUserName);
        main.add(new JLabel("Password:"));
        main.add(txtPassword);

        SpringUtilities.makeCompactGrid(main,
                3, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad

        this.getContentPane().add(main);
        this.getContentPane().add(btnLogin);
        this.btnLogin.addActionListener(e -> {
            String username = getTxtUserName().getText().trim();
            String password = getTxtPassword().getText().trim();
            String userType = (String) getUserTypeComboBox().getSelectedItem();

//            System.out.println("Login with username = " + username + " and password = " + password);
            User user = null;
            try {
                user = loginController.login(userType, username, password);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            if (user == null) {
                JOptionPane.showMessageDialog(null, "Credentials/UserType mismatch!");
            }
            else {
                Application.getInstance().setCurrentUser(user);
                setVisible(false);
                switch (user.getUserType()) {
                    case "CUSTOMER" ->
//                    Application.getInstance().getCustomerView().loadUserData();
                            Application.getInstance().getCustomerView().setVisible(true);
                    case "RESTAURANT" ->
//                    Application.getInstance().getRestaurantView().loadUserData();
                            Application.getInstance().getRestaurantView().setVisible(true);
                    case "DELIVERY" ->
//                    Application.getInstance().getDeliveryAgentView().loadUserData();
                            {
                                Application.getInstance().getDeliveryAgentView().setVisible(true);
                                Application.getInstance().getDeliveryAgentView().loadData();
                            }
                }
//                JOptionPane.showMessageDialog(null, "Login Successful!");
            }
        });
    }

    public JButton getBtnLogin() {
        return btnLogin;
    }

    public JTextField getTxtPassword() {
        return txtPassword;
    }

    public JTextField getTxtUserName() {
        return txtUserName;
    }

    public JComboBox<String> getUserTypeComboBox() {
        return userTypeComboBox;
    }

}
