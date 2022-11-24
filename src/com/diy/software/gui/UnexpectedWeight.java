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
		
		JTextArea txtrAnUnexpectedWeight = new JTextArea();
		txtrAnUnexpectedWeight.setBackground(new Color(192, 192, 192));
		txtrAnUnexpectedWeight.setWrapStyleWord(true);
		txtrAnUnexpectedWeight.setFont(new Font("Monospaced", Font.PLAIN, 14));
		txtrAnUnexpectedWeight.setLineWrap(true);
		txtrAnUnexpectedWeight.setText("An unexpected weight was detected! Please wait for assistance.");
		txtrAnUnexpectedWeight.setEditable(false);
		txtrAnUnexpectedWeight.setBounds(99, 150, 245, 199);
		add(txtrAnUnexpectedWeight);

	}
}
