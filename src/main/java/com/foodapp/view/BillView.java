package com.foodapp.view;

import com.foodapp.Application;
import com.foodapp.controller.BillController;
import com.foodapp.controller.DishController;
import com.foodapp.model.Bill;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BillView extends JFrame {
    private BillController billController;
    private JPanel billPanel;
    private JTextField tname  = new JTextField(20);
    private JTextField address  = new JTextField(20);
    private JTextField phoneNumber  = new JTextField(20);
    private JTextField cardNumber  = new JTextField(20);
    private JTextField cvv  = new JTextField(20);
    private JComboBox<String> fmonthComboBox;
    private JComboBox<String> fyearComboBox;
    private JComboBox<String> tmonthComboBox;
    private JComboBox<String> tyearComboBox;
    private String fm;
    private String tm;
    private int fy;
    private int ty;
    private Map<String, Integer> monthMap;
    public BillView(BillController billController){
        this.billController = billController;
        this.setupLayout();
    }

    private void setupLayout() {
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        JLabel title = new JLabel("Billing Page");
        title.setFont(new Font("Sans Serif", Font.BOLD, 24));
        JPanel panelTitle = new JPanel();
        panelTitle.add(title);
        this.getContentPane().add(panelTitle);

        billPanel = new JPanel();
        billPanel.setLayout(new BoxLayout(billPanel, BoxLayout.Y_AXIS));

        JScrollPane billPane = new JScrollPane(billPanel);
        this.getContentPane().add(billPane);

        JPanel panelname = new JPanel();
        panelname.add(new JLabel("Name: "));
        panelname.add(tname);
        billPanel.add(panelname);

        JPanel paneladdress = new JPanel();
        paneladdress.add(new JLabel("Address: "));
        paneladdress.add(address);
        billPanel.add(paneladdress);

        JPanel panelno = new JPanel();
        panelno.add(new JLabel("Phone Number: "));
        panelno.add(phoneNumber);
        billPanel.add(panelno);

        JPanel panelcno = new JPanel();
        panelcno.add(new JLabel("Card Number: "));
        panelcno.add(cardNumber);
        billPanel.add(panelcno);

        JPanel panelcvv = new JPanel();
        panelcvv.add(new JLabel("CVV: "));
        panelcvv.add(cvv);
        billPanel.add(panelcvv);

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        String[] fyears = new String[10];
        for (int i = 0; i < 10; i++) {
            fyears[i] = String.valueOf(currentYear - i);
        }
        String[] tyears = new String[10];
        for (int i = 0; i < 10; i++) {
            tyears[i] = String.valueOf(currentYear + i);
        }
        fmonthComboBox = new JComboBox<>(months);
        fyearComboBox = new JComboBox<>(fyears);
        tmonthComboBox = new JComboBox<>(months);
        tyearComboBox = new JComboBox<>(tyears);

        billPanel.add(new JLabel("From Month:"));
        billPanel.add(fmonthComboBox);
        fmonthComboBox.addActionListener(e -> {
            fm = (String) getFmonthComboBox().getSelectedItem();
        });

        billPanel.add(new JLabel("From Year:"));
        billPanel.add(fyearComboBox);
        fyearComboBox.addActionListener(e -> {
            fy =Integer.parseInt((String) getFyearComboBox().getSelectedItem());
        });

        billPanel.add(new JLabel("To Month:"));
        billPanel.add(tmonthComboBox);
        tmonthComboBox.addActionListener(e -> {
            tm = (String) getFmonthComboBox().getSelectedItem();
        });

        billPanel.add(new JLabel("To Year:"));
        billPanel.add(tyearComboBox);
        tyearComboBox.addActionListener(e -> {
            ty =Integer.parseInt((String) getFyearComboBox().getSelectedItem());
        });

        JButton button = new JButton("Pay Bill");
        button.addActionListener(e -> {
            paybill();
        });
        billPanel.add(button, BorderLayout.SOUTH);
    }

    private void paybill(){

        monthMap = new HashMap<>();
        monthMap.put("January", Calendar.JANUARY + 1);
        monthMap.put("February", Calendar.FEBRUARY + 1);
        monthMap.put("March", Calendar.MARCH + 1);
        monthMap.put("April", Calendar.APRIL + 1);
        monthMap.put("May", Calendar.MAY + 1);
        monthMap.put("June", Calendar.JUNE + 1);
        monthMap.put("July", Calendar.JULY + 1);
        monthMap.put("August", Calendar.AUGUST + 1);
        monthMap.put("September", Calendar.SEPTEMBER + 1);
        monthMap.put("October", Calendar.OCTOBER + 1);
        monthMap.put("November", Calendar.NOVEMBER + 1);
        monthMap.put("December", Calendar.DECEMBER + 1);

        String token = Application.getInstance().getToken();
        int id = Application.getInstance().getUserId();

        Bill bill = new Bill();
        bill.setOrderId(Application.getInstance().getOrderId());
        bill.setName(tname.getText().trim());
        bill.setAddress(address.getText().trim());
        bill.setPhoneNumber(Long.parseLong(phoneNumber.getText()));
        bill.setCardNumber(Long.parseLong(cardNumber.getText()));
        bill.setCvv(Integer.parseInt(cvv.getText()));
        bill.setFromMM(monthMap.get(fm));
        bill.setFromYYYY(fy);
        bill.setToMM(monthMap.get(tm));
        bill.setToYYYY(ty);
        bill.setAmount(Application.getInstance().getTotalCost());

        try {
            billController.payorder(token, id, bill);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }

        Application.getInstance().getBillView().setVisible(false);
        Application.getInstance().getOrderView().loadData();
        Application.getInstance().getOrderView().setVisible(true);
    }

    public JTextField getTname() {
        return tname;
    }

    public void setTname(JTextField name) {
        this.tname = tname;
    }

    public JTextField getAddress() {
        return address;
    }

    public void setAddress(JTextField address) {
        this.address = address;
    }

    public JTextField getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(JTextField phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public JTextField getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(JTextField cardNumber) {
        this.cardNumber = cardNumber;
    }

    public JTextField getCvv() {
        return cvv;
    }

    public void setCvv(JTextField cvv) {
        this.cvv = cvv;
    }

    public JComboBox<String> getFmonthComboBox() {
        return fmonthComboBox;
    }

    public void setFmonthComboBox(JComboBox<String> fmonthComboBox) {
        this.fmonthComboBox = fmonthComboBox;
    }

    public JComboBox<String> getFyearComboBox() {
        return fyearComboBox;
    }

    public void setFyearComboBox(JComboBox<String> fyearComboBox) {
        this.fyearComboBox = fyearComboBox;
    }

    public JComboBox<String> getTmonthComboBox() {
        return tmonthComboBox;
    }

    public void setTmonthComboBox(JComboBox<String> tmonthComboBox) {
        this.tmonthComboBox = tmonthComboBox;
    }

    public JComboBox<String> getTyearComboBox() {
        return tyearComboBox;
    }

    public void setTyearComboBox(JComboBox<String> tyearComboBox) {
        this.tyearComboBox = tyearComboBox;
    }
}
