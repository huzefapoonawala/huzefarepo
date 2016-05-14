package com.dynamicsext.ig.gui;


import java.awt.EventQueue;
import java.awt.BorderLayout;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;

import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.commons.lang3.StringUtils;

import com.dynamicsext.ig.util.CommonUtil;
import com.dynamicsext.ig.util.NumberFormatter;
import com.dynamicsext.ig.vo.InvoiceAmounts;
import com.dynamicsext.ig.vo.Tender;

public class TenderUI extends JFrame {
	
	private static final long serialVersionUID = -8032151728821282454L;
	private TenderUI this_;
	private JTextField tTenderDesc;
	private JButton btnOK;
	
	private ReceiptGeneratorService receiptGeneratorService;
	private JButton btnCancel;
	private JLabel totalDueVal;
	private JLabel totalTenderVal;
	private JLabel balVal;
	
	private InvoiceAmounts ia;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TenderUI frame = new TenderUI();
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
	public TenderUI() {
		this_ = this;
		CommonUtil.setEscapeImpl(this_.getRootPane());
		setResizable(false);
		setTitle("Tender");
		setBounds(100, 100, 408, 190);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{512, 0};
		gridBagLayout.rowHeights = new int[]{60, 96, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JPanel tDetailsPane = new JPanel();
		GridBagConstraints gbc_tDetailsPane = new GridBagConstraints();
		gbc_tDetailsPane.anchor = GridBagConstraints.BASELINE;
		gbc_tDetailsPane.fill = GridBagConstraints.HORIZONTAL;
		gbc_tDetailsPane.insets = new Insets(0, 0, 5, 0);
		gbc_tDetailsPane.gridx = 0;
		gbc_tDetailsPane.gridy = 0;
		getContentPane().add(tDetailsPane, gbc_tDetailsPane);
		GridBagLayout gbl_tDetailsPane = new GridBagLayout();
		gbl_tDetailsPane.columnWidths = new int[] {63, 256};
		gbl_tDetailsPane.rowHeights = new int[] {36};
		gbl_tDetailsPane.columnWeights = new double[]{0.0, 1.0};
		gbl_tDetailsPane.rowWeights = new double[]{0.0};
		tDetailsPane.setLayout(gbl_tDetailsPane);
		
		JLabel lblCash = new JLabel(" Cash");
		lblCash.setHorizontalAlignment(SwingConstants.LEFT);
		lblCash.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_lblCash = new GridBagConstraints();
		gbc_lblCash.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblCash.insets = new Insets(0, 0, 5, 5);
		gbc_lblCash.gridx = 0;
		gbc_lblCash.gridy = 0;
		tDetailsPane.add(lblCash, gbc_lblCash);
		
		tTenderDesc = new JTextField();
		tTenderDesc.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tTenderDesc.setHorizontalAlignment(SwingConstants.LEFT);
		tTenderDesc.addActionListener(new AbstractAction() {
			
			private static final long serialVersionUID = -8997701539485138834L;

			public void actionPerformed(ActionEvent e) {
				btnOK.doClick();
			}
		});
		GridBagConstraints gbc_tTenderDesc = new GridBagConstraints();
		gbc_tTenderDesc.anchor = GridBagConstraints.NORTHWEST;
		gbc_tTenderDesc.insets = new Insets(0, 0, 5, 0);
		gbc_tTenderDesc.gridx = 1;
		gbc_tTenderDesc.gridy = 0;
		tDetailsPane.add(tTenderDesc, gbc_tTenderDesc);
		tTenderDesc.setColumns(25);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		getContentPane().add(panel, gbc_panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		panel_1.setForeground(Color.WHITE);
		panel_1.setBackground(Color.BLACK);
		panel.add(panel_1, BorderLayout.NORTH);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 43, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		JLabel lblTotalDue = new JLabel("Total Due:");
		lblTotalDue.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTotalDue.setForeground(Color.WHITE);
		GridBagConstraints gbc_lblTotalDue = new GridBagConstraints();
		gbc_lblTotalDue.anchor = GridBagConstraints.WEST;
		gbc_lblTotalDue.insets = new Insets(0, 0, 5, 5);
		gbc_lblTotalDue.gridx = 0;
		gbc_lblTotalDue.gridy = 0;
		panel_1.add(lblTotalDue, gbc_lblTotalDue);
		
		totalDueVal = new JLabel("$0.00");
		totalDueVal.setFont(new Font("Tahoma", Font.PLAIN, 13));
		totalDueVal.setForeground(Color.WHITE);
		GridBagConstraints gbc_totalDueVal = new GridBagConstraints();
		gbc_totalDueVal.anchor = GridBagConstraints.EAST;
		gbc_totalDueVal.insets = new Insets(0, 0, 5, 0);
		gbc_totalDueVal.gridx = 1;
		gbc_totalDueVal.gridy = 0;
		panel_1.add(totalDueVal, gbc_totalDueVal);
		
		JLabel lblTotalTendered = new JLabel("Total Tendered:");
		lblTotalTendered.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTotalTendered.setForeground(Color.WHITE);
		GridBagConstraints gbc_lblTotalTendered = new GridBagConstraints();
		gbc_lblTotalTendered.anchor = GridBagConstraints.WEST;
		gbc_lblTotalTendered.insets = new Insets(0, 0, 5, 5);
		gbc_lblTotalTendered.gridx = 0;
		gbc_lblTotalTendered.gridy = 1;
		panel_1.add(lblTotalTendered, gbc_lblTotalTendered);
		
		totalTenderVal = new JLabel("$0.00");
		totalTenderVal.setFont(new Font("Tahoma", Font.PLAIN, 13));
		totalTenderVal.setForeground(Color.WHITE);
		GridBagConstraints gbc_totalTenderVal = new GridBagConstraints();
		gbc_totalTenderVal.anchor = GridBagConstraints.EAST;
		gbc_totalTenderVal.insets = new Insets(0, 0, 5, 0);
		gbc_totalTenderVal.gridx = 1;
		gbc_totalTenderVal.gridy = 1;
		panel_1.add(totalTenderVal, gbc_totalTenderVal);
		
		JLabel lblBalance = new JLabel("Balance:");
		lblBalance.setForeground(Color.WHITE);
		lblBalance.setFont(new Font("Tahoma", Font.PLAIN, 24));
		GridBagConstraints gbc_lblBalance = new GridBagConstraints();
		gbc_lblBalance.anchor = GridBagConstraints.WEST;
		gbc_lblBalance.insets = new Insets(0, 0, 0, 5);
		gbc_lblBalance.gridx = 0;
		gbc_lblBalance.gridy = 2;
		panel_1.add(lblBalance, gbc_lblBalance);
		
		balVal = new JLabel("$0.00");
		balVal.setForeground(Color.RED);
		balVal.setFont(new Font("Tahoma", Font.PLAIN, 24));
		GridBagConstraints gbc_balVal = new GridBagConstraints();
		gbc_balVal.anchor = GridBagConstraints.EAST;
		gbc_balVal.gridx = 1;
		gbc_balVal.gridy = 2;
		panel_1.add(balVal, gbc_balVal);
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.EAST);
		panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Tender tender = null;
				if (StringUtils.isNotBlank(tTenderDesc.getText())) {
					try {
						Double tenderAmt = Double.valueOf(tTenderDesc.getText());
						tender = new Tender();
						tender.setTenderAmount(tenderAmt);
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(null, "Invalid tender amount, please enter valid value", "", JOptionPane.ERROR_MESSAGE);
						tTenderDesc.requestFocus();
					}
				}
				else{
					tender = new Tender();
					tender.setTenderAmount(ia.getGrandTotalAmount());
				}
				if (tender != null) {
					tender.setBalanceAmount(tender.getTenderAmount() - NumberFormatter.getInstance().round(ia.getGrandTotalAmount(), 2));
					totalTenderVal.setText(ia.getAmountWithCurency(tender.getTenderAmount()));
					balVal.setText(ia.getAmountWithCurency(tender.getBalanceAmount()));
					if (tender.getBalanceAmount() < 0) {
						tender = null;
						JOptionPane.showMessageDialog(null, "Insufficient amount tendered", "", JOptionPane.ERROR_MESSAGE);
						tTenderDesc.requestFocus();
					} else {
						ia.setTender(tender);
						receiptGeneratorService.generateReceipt(tender);
					}
				}
			}
		});
		btnOK.registerKeyboardAction(new ButtonEnterActionListener(btnOK), KeyStroke.getKeyStroke("ENTER"), JComponent.WHEN_FOCUSED);
		panel_2.add(btnOK);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				dispatchEvent(new WindowEvent(this_, WindowEvent.WINDOW_CLOSING));
			}
		});
		btnCancel.registerKeyboardAction(new ButtonEnterActionListener(btnCancel), KeyStroke.getKeyStroke("ENTER"), JComponent.WHEN_FOCUSED);
		panel_2.add(btnCancel);
		this_.addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				tTenderDesc.setText(null);
				tTenderDesc.requestFocus();
			}
		});
	}
	
	public TenderUI(ReceiptGeneratorService receiptGeneratorService){
		this();
		this.receiptGeneratorService = receiptGeneratorService;
	}
	
	public void closeWindow() {
		btnCancel.doClick();
	}
	
	public void showTenderWindow(InvoiceAmounts ia) {
		this.ia = ia;
		this_.setVisible(true);
		totalDueVal.setText(ia.getAmountWithCurency(ia.getGrandTotalAmount()));
		balVal.setText(ia.getAmountWithCurency(ia.getGrandTotalAmount()));
		totalTenderVal.setText(ia.getAmountWithCurency(0));
	}
}
