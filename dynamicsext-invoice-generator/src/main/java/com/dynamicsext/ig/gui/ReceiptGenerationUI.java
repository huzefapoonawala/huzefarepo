package com.dynamicsext.ig.gui;

import java.awt.EventQueue;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;

import javax.swing.SwingConstants;

import java.awt.Color;

import javax.swing.JButton;

import com.dynamicsext.ig.util.CommonUtil;
import com.dynamicsext.ig.vo.InvoiceAmounts;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ReceiptGenerationUI extends JFrame {

	private static final long serialVersionUID = -3418249203577902192L;

	private JPanel contentPane;
	private ReceiptGenerationUI this_;
	private JButton btnClose;
	private JLabel lblProcessingMessage;
	
	private static final String GENERATE_RECEIPT_MSG = " Generating receipt...";
	private static final String TRANS_COMPLETE_MSG = " Transaction completed.";
	private JLabel tenderVal;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ReceiptGenerationUI frame = new ReceiptGenerationUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ReceiptGenerationUI() {
		this_ = this;
		CommonUtil.setEscapeImpl(this_.getRootPane());
		setResizable(false);
		setTitle("Post Transaction");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 380, 190);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 45, 0, 39, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblStatus = new JLabel("Status:");
		lblStatus.setFont(new Font("Tahoma", Font.BOLD, 13));
		GridBagConstraints gbc_lblStatus = new GridBagConstraints();
		gbc_lblStatus.anchor = GridBagConstraints.WEST;
		gbc_lblStatus.insets = new Insets(0, 0, 5, 0);
		gbc_lblStatus.gridx = 0;
		gbc_lblStatus.gridy = 0;
		contentPane.add(lblStatus, gbc_lblStatus);
		
		lblProcessingMessage = new JLabel(GENERATE_RECEIPT_MSG);
		lblProcessingMessage.setFont(new Font("Tahoma", Font.PLAIN, 22));
		GridBagConstraints gbc_lblProcessingMessage = new GridBagConstraints();
		gbc_lblProcessingMessage.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblProcessingMessage.insets = new Insets(0, 0, 5, 0);
		gbc_lblProcessingMessage.gridx = 0;
		gbc_lblProcessingMessage.gridy = 1;
		contentPane.add(lblProcessingMessage, gbc_lblProcessingMessage);
		
		JLabel lblChange = new JLabel("Change:");
		lblChange.setFont(new Font("Tahoma", Font.BOLD, 13));
		GridBagConstraints gbc_lblChange = new GridBagConstraints();
		gbc_lblChange.insets = new Insets(0, 0, 5, 0);
		gbc_lblChange.anchor = GridBagConstraints.WEST;
		gbc_lblChange.gridx = 0;
		gbc_lblChange.gridy = 2;
		contentPane.add(lblChange, gbc_lblChange);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.NORTH;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 3;
		contentPane.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{193, 212, 0};
		gbl_panel.rowHeights = new int[] {36};
		gbl_panel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0};
		panel.setLayout(gbl_panel);
		
		JLabel lblCash = new JLabel(" Cash");
		lblCash.setFont(new Font("Tahoma", Font.PLAIN, 22));
		GridBagConstraints gbc_lblCash = new GridBagConstraints();
		gbc_lblCash.anchor = GridBagConstraints.NORTH;
		gbc_lblCash.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblCash.insets = new Insets(0, 0, 0, 5);
		gbc_lblCash.gridx = 0;
		gbc_lblCash.gridy = 0;
		panel.add(lblCash, gbc_lblCash);
		
		tenderVal = new JLabel("$0.00 ");
		tenderVal.setVerticalAlignment(SwingConstants.TOP);
		tenderVal.setFont(new Font("Tahoma", Font.PLAIN, 22));
		tenderVal.setForeground(Color.GREEN);
		tenderVal.setBackground(Color.BLACK);
		tenderVal.setHorizontalAlignment(SwingConstants.RIGHT);
		tenderVal.setOpaque(true);
		GridBagConstraints gbc_tenderVal = new GridBagConstraints();
		gbc_tenderVal.fill = GridBagConstraints.BOTH;
		gbc_tenderVal.gridx = 1;
		gbc_tenderVal.gridy = 0;
		panel.add(tenderVal, gbc_tenderVal);
		
		btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//dispatchEvent(new WindowEvent(this_, WindowEvent.WINDOW_CLOSING));
				this_.setVisible(false);
			}
		});
		btnClose.registerKeyboardAction(new ButtonEnterActionListener(btnClose), KeyStroke.getKeyStroke("ENTER"), JComponent.WHEN_FOCUSED);
		GridBagConstraints gbc_btnClose = new GridBagConstraints();
		gbc_btnClose.anchor = GridBagConstraints.EAST;
		gbc_btnClose.gridx = 0;
		gbc_btnClose.gridy = 4;
		contentPane.add(btnClose, gbc_btnClose);
		
		this_.addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				lblProcessingMessage.setText(GENERATE_RECEIPT_MSG);
			}
		});
	}
	
	public void setTransactionCompleteMsg() {
		lblProcessingMessage.setText(TRANS_COMPLETE_MSG);
		btnClose.requestFocus();
	}
	
	public void showWindow(InvoiceAmounts ia) {
		this_.setVisible(true);
		tenderVal.setText(ia.getAmountWithCurency(ia.getTender().getBalanceAmount()));
	}

}
