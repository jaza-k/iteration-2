package com.diy.software.gui;

import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JTextArea;

public class UnexpectedWeight extends JPanel {

	/**
	 * Create the panel.
	 */
	public UnexpectedWeight() {
		setBackground(new Color(240, 128, 128));
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Please wait for assistance!");
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setFont(new Font("Georgia", Font.PLAIN, 15));
		lblNewLabel.setBackground(new Color(255, 255, 255));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(126, 181, 221, 161);
		add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Station Blocked!");
		lblNewLabel_1.setForeground(new Color(255, 255, 255));
		lblNewLabel_1.setFont(new Font("Georgia", Font.PLAIN, 14));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(116, 216, 221, 17);
		add(lblNewLabel_1);

	}
}
