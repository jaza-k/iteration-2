package com.diy.software.gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AddInkPopup extends JDialog implements ActionListener {
    private final JPanel addInkContentPanel = new JPanel();
    private final JFrame addInkFrame = new JFrame();
    private JLabel lowInk;
    private JButton ok;

    public AddInkPopup(){
        addInkFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 300);
        addInkContentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(addInkContentPanel);
        addInkContentPanel.setLayout(new BoxLayout(addInkContentPanel, BoxLayout.Y_AXIS));

        addInkContentPanel.add(Box.createRigidArea(new Dimension(0, 90)));

        lowInk = new JLabel("Station's printer is low on ink, fill it up please :)");
        lowInk.setHorizontalAlignment(JLabel.CENTER);
        lowInk.setAlignmentX(CENTER_ALIGNMENT);
        addInkContentPanel.add(lowInk);
        addInkContentPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        ok = new JButton("ok");
        ok.setAlignmentX(CENTER_ALIGNMENT);
        ok.setPreferredSize(new Dimension(100, 10));
        ok.addActionListener(this);
        ok.setBackground(Color.LIGHT_GRAY);
        addInkContentPanel.add(ok);

        setLocationRelativeTo(null);
        setResizable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
    }
}
