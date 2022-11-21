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

public class AddPaperPopup extends JDialog implements ActionListener {
    private final JPanel addPaperContentPanel = new JPanel();
    private final JFrame addPaperFrame = new JFrame();
    private JLabel lowPaper;
    private JButton ok;

    public AddPaperPopup(){
        addPaperFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 300);
        addPaperContentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(addPaperContentPanel);
        addPaperContentPanel.setLayout(new BoxLayout(addPaperContentPanel, BoxLayout.Y_AXIS));

        addPaperContentPanel.add(Box.createRigidArea(new Dimension(0, 90)));

        lowPaper = new JLabel("Station's printer is low on paper, fill it up please :)");
        lowPaper.setHorizontalAlignment(JLabel.CENTER);
        lowPaper.setAlignmentX(CENTER_ALIGNMENT);
        addPaperContentPanel.add(lowPaper);
        addPaperContentPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        ok = new JButton("ok");
        ok.setAlignmentX(CENTER_ALIGNMENT);
        ok.setPreferredSize(new Dimension(100, 10));
        ok.addActionListener(this);
        ok.setBackground(Color.LIGHT_GRAY);
        addPaperContentPanel.add(ok);

        setLocationRelativeTo(null);
        setResizable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
    }
}
