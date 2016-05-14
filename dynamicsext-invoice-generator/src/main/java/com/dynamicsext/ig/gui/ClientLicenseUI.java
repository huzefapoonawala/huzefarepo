package com.dynamicsext.ig.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Color;
import java.awt.Font;

public class ClientLicenseUI {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientLicenseUI window = new ClientLicenseUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientLicenseUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setResizable(false);
		frame.setTitle("Dynamics Invoice Generator");
		frame.setBounds(100, 100, 693, 116);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{540, 0};
		gridBagLayout.rowHeights = new int[]{98, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JLabel lblOopsInvalidClient = new JLabel("Oops!!! Invalid client license, please provide a valid license...");
		lblOopsInvalidClient.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblOopsInvalidClient.setForeground(Color.RED);
		GridBagConstraints gbc_lblOopsInvalidClient = new GridBagConstraints();
		gbc_lblOopsInvalidClient.gridx = 0;
		gbc_lblOopsInvalidClient.gridy = 0;
		frame.getContentPane().add(lblOopsInvalidClient, gbc_lblOopsInvalidClient);
	}

	public JFrame getFrame() {
		return frame;
	}
}
