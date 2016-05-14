package com.dynamicsext.ig.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.BorderLayout;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dynamicsext.ig.dao.InvoiceGeneratorDAO;
import com.dynamicsext.ig.util.CommonUtil;
import com.dynamicsext.ig.util.InvoiceGeneratorConfigs;
import com.dynamicsext.ig.util.LicenseValidator;
import com.dynamicsext.ig.vo.Customer;
import com.dynamicsext.ig.vo.InvoiceAmounts;
import com.dynamicsext.ig.vo.Item;
import com.dynamicsext.ig.vo.Tender;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;

import javax.swing.JButton;

import java.awt.event.ActionListener;

public class InvoiceGeneratorClient implements ReceiptGeneratorService {

	private static final Logger LOG = LoggerFactory.getLogger(InvoiceGeneratorClient.class);
	private static final String APP_CONTEXT_PATH = "ig-context.xml"; 
	
	private static AbstractApplicationContext CTX;
	
	private InvoiceGeneratorDAO invoiceGeneratorDAO;
	
	private JFrame frmDynamicsInvoiceGenerator;
	private JTextArea txtShipTo;
	private JTextArea txtBillTo;
	private JTable itemDataGrid;
	
	private TenderUI tenderUI;
	
	private List<Customer> customerList;
	private JComboBox<Customer> customerRec;
	
	private int deletedRowIdx = -1;
	private boolean isInvalidItemCode = false;
	
	private InvoiceGeneratorClient this_;
	
	private ReceiptGenerationUI receiptGenerationUI;
	private JLabel subTotalVal;
	private JLabel salesTaxVal;
	private JLabel grandTotalVal;
	
	private InvoiceAmounts ia;
	
	private List<Item> invoicedItems;
	
	private InvoicePrintUI invoicePrinter;
	private JButton btnClearCustomer;
	
	private ItemSearchUI itemSearchUI;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		if (!LicenseValidator.validateLicense()) {
			new ClientLicenseUI().getFrame().setVisible(true);
		}
		else{
			LOG.info("Invoice generator client started");
			CTX = new ClassPathXmlApplicationContext(APP_CONTEXT_PATH);
			CTX.start();
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						InvoiceGeneratorClient window = new InvoiceGeneratorClient();
						window.frmDynamicsInvoiceGenerator.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	/**
	 * Create the application.
	 * @wbp.parser.entryPoint
	 */
	public InvoiceGeneratorClient() {
		this_ = this;
		invoiceGeneratorDAO = CTX.getBean(InvoiceGeneratorDAO.class);
		loadMasterData();
		initialize();
		CommonUtil.setEscapeImpl(frmDynamicsInvoiceGenerator.getRootPane());
		setF2KeyImpl(frmDynamicsInvoiceGenerator.getRootPane());
	}
	
	private void loadMasterData(){
		customerList = invoiceGeneratorDAO.getAllCompanies();
		Customer blankCust = new Customer();
		blankCust.setCompany("");
		customerList.add(0, blankCust);
		invoicedItems = new ArrayList<Item>();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initialize() {
		frmDynamicsInvoiceGenerator = new JFrame();
		frmDynamicsInvoiceGenerator.setTitle("Dynamics Invoice Generator");
		frmDynamicsInvoiceGenerator.setBounds(100, 100, 1395, 804);
		frmDynamicsInvoiceGenerator.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent){
				CTX.stop();
				LOG.info("Invoice generator client ended");
				System.exit(0);
			}
		});
		
		JPanel custPane = new JPanel();
		custPane.setBorder(null);
		frmDynamicsInvoiceGenerator.getContentPane().add(custPane, BorderLayout.NORTH);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{684, 689, 0};
		gridBagLayout.rowHeights = new int[]{42, 14, 123, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		custPane.setLayout(gridBagLayout);
		
		JPanel selCustPane = new JPanel();
		GridBagConstraints gbc_selCustPane = new GridBagConstraints();
		gbc_selCustPane.fill = GridBagConstraints.BOTH;
		gbc_selCustPane.insets = new Insets(0, 0, 5, 5);
		gbc_selCustPane.gridx = 0;
		gbc_selCustPane.gridy = 0;
		custPane.add(selCustPane, gbc_selCustPane);
		GridBagLayout gbl_selCustPane = new GridBagLayout();
		gbl_selCustPane.columnWidths = new int[]{0, 430, 0, 0};
		gbl_selCustPane.rowHeights = new int[]{0, 0};
		gbl_selCustPane.columnWeights = new double[]{0.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_selCustPane.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		selCustPane.setLayout(gbl_selCustPane);
		
		JLabel lblSelectCustomer = new JLabel(" Select Customer");
		lblSelectCustomer.setFont(new Font("Tahoma", Font.BOLD, 13));
		GridBagConstraints gbc_lblSelectCustomer = new GridBagConstraints();
		gbc_lblSelectCustomer.anchor = GridBagConstraints.WEST;
		gbc_lblSelectCustomer.insets = new Insets(0, 0, 0, 5);
		gbc_lblSelectCustomer.gridx = 0;
		gbc_lblSelectCustomer.gridy = 0;
		selCustPane.add(lblSelectCustomer, gbc_lblSelectCustomer);
		
		customerRec = new JComboBox(customerList.toArray());
		customerRec.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Customer customer = (Customer) e.getItem();
					StringBuilder billToTxt = new StringBuilder();
					if (StringUtils.isNotBlank(customer.toString())) {
						billToTxt
							.append(customer.getFirstName()).append(" ").append(customer.getLastName()).append("\n")
							.append(customer.getCompany()).append("\n")
							.append(customer.getAddress()).append(StringUtils.isNotBlank(customer.getAddress2()) ? ", ": "").append(StringUtils.defaultIfBlank(customer.getAddress2(), "")).append("\n")
							.append(customer.getCity()).append(", ").append(customer.getState()).append(" ").append(customer.getZip());
					}
					txtBillTo.setText(billToTxt.toString());
					txtShipTo.setText(billToTxt.toString());
				}				
			}
		});
		GridBagConstraints gbc_customerRec = new GridBagConstraints();
		gbc_customerRec.insets = new Insets(0, 0, 0, 5);
		gbc_customerRec.fill = GridBagConstraints.HORIZONTAL;
		gbc_customerRec.gridx = 1;
		gbc_customerRec.gridy = 0;
		selCustPane.add(customerRec, gbc_customerRec);
		
		btnClearCustomer = new JButton("Clear Customer");
		btnClearCustomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				customerRec.setSelectedIndex(0);
				customerRec.requestFocus();
			}
		});
		btnClearCustomer.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), "OnEnterPressed");
		btnClearCustomer.getActionMap().put("OnEnterPressed", new AbstractAction() {
			
			private static final long serialVersionUID = 4536371361665958499L;

			public void actionPerformed(ActionEvent e) {
				btnClearCustomer.doClick();
			}
		});
		btnClearCustomer.setFont(new Font("Tahoma", Font.PLAIN, 13));
		GridBagConstraints gbc_btnClearCustomer = new GridBagConstraints();
		gbc_btnClearCustomer.anchor = GridBagConstraints.WEST;
		gbc_btnClearCustomer.gridx = 2;
		gbc_btnClearCustomer.gridy = 0;
		selCustPane.add(btnClearCustomer, gbc_btnClearCustomer);
		
		JLabel lblNewLabel = new JLabel("");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 0;
		custPane.add(lblNewLabel, gbc_lblNewLabel);
		
		JLabel lblBillTo = new JLabel(" Bill To");
		lblBillTo.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblBillTo.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblBillTo = new GridBagConstraints();
		gbc_lblBillTo.anchor = GridBagConstraints.WEST;
		gbc_lblBillTo.insets = new Insets(0, 0, 5, 5);
		gbc_lblBillTo.gridx = 0;
		gbc_lblBillTo.gridy = 1;
		custPane.add(lblBillTo, gbc_lblBillTo);
		
		JLabel lblShipTo = new JLabel(" Ship To");
		lblShipTo.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblShipTo.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblShipTo = new GridBagConstraints();
		gbc_lblShipTo.anchor = GridBagConstraints.WEST;
		gbc_lblShipTo.fill = GridBagConstraints.VERTICAL;
		gbc_lblShipTo.insets = new Insets(0, 0, 5, 0);
		gbc_lblShipTo.gridx = 1;
		gbc_lblShipTo.gridy = 1;
		custPane.add(lblShipTo, gbc_lblShipTo);
		
		txtBillTo = new JTextArea();
		txtBillTo.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtBillTo.setDisabledTextColor(Color.BLACK);
		txtBillTo.setEnabled(false);
		txtBillTo.setEditable(false);
		txtBillTo.setLineWrap(true);
		txtBillTo.setWrapStyleWord(true);
		txtBillTo.setColumns(20);
		txtBillTo.setRows(5);
		GridBagConstraints gbc_txtBillTo = new GridBagConstraints();
		gbc_txtBillTo.fill = GridBagConstraints.BOTH;
		gbc_txtBillTo.insets = new Insets(0, 0, 0, 5);
		gbc_txtBillTo.gridx = 0;
		gbc_txtBillTo.gridy = 2;
		custPane.add(txtBillTo, gbc_txtBillTo);
		
		txtShipTo = new JTextArea();
		txtShipTo.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtShipTo.setDisabledTextColor(Color.BLACK);
		txtShipTo.setEnabled(false);
		txtShipTo.setEditable(false);
		txtShipTo.setLineWrap(true);
		txtShipTo.setWrapStyleWord(true);
		txtShipTo.setColumns(20);
		txtShipTo.setRows(5);
		GridBagConstraints gbc_txtShipTo = new GridBagConstraints();
		gbc_txtShipTo.fill = GridBagConstraints.BOTH;
		gbc_txtShipTo.gridx = 1;
		gbc_txtShipTo.gridy = 2;
		custPane.add(txtShipTo, gbc_txtShipTo);
		
		JScrollPane scrollPane = new JScrollPane();
		frmDynamicsInvoiceGenerator.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		initializeItemDataGrid();
		scrollPane.setViewportView(itemDataGrid);
		
		JPanel panel_3 = new JPanel();
		frmDynamicsInvoiceGenerator.getContentPane().add(panel_3, BorderLayout.SOUTH);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		JPanel pricePane = new JPanel();
		panel_3.add(pricePane, BorderLayout.NORTH);
		pricePane.setBackground(Color.LIGHT_GRAY);
		GridBagLayout gbl_pricePane = new GridBagLayout();
		gbl_pricePane.columnWidths = new int[] {488, 470, 470, 0};
		gbl_pricePane.rowHeights = new int[] {};
		gbl_pricePane.columnWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_pricePane.rowWeights = new double[]{1.0};
		pricePane.setLayout(gbl_pricePane);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.BLACK);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 0, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		pricePane.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.rowHeights = new int[]{16, 0};
		gbl_panel.columnWeights = new double[]{1.0};
		gbl_panel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblSubTotal = new JLabel(" Sub Total");
		GridBagConstraints gbc_lblSubTotal = new GridBagConstraints();
		gbc_lblSubTotal.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblSubTotal.insets = new Insets(0, 0, 0, 5);
		gbc_lblSubTotal.gridx = 0;
		gbc_lblSubTotal.gridy = 0;
		panel.add(lblSubTotal, gbc_lblSubTotal);
		lblSubTotal.setForeground(Color.RED);
		lblSubTotal.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblSubTotal.setHorizontalAlignment(SwingConstants.LEFT);
		
		subTotalVal = new JLabel("$ 0.00  ");
		subTotalVal.setForeground(Color.RED);
		subTotalVal.setFont(new Font("Tahoma", Font.BOLD, 30));
		subTotalVal.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_subTotalVal = new GridBagConstraints();
		gbc_subTotalVal.anchor = GridBagConstraints.EAST;
		gbc_subTotalVal.insets = new Insets(0, 0, 5, 5);
		gbc_subTotalVal.gridx = 0;
		gbc_subTotalVal.gridy = 1;
		panel.add(subTotalVal, gbc_subTotalVal);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.BLACK);
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 0, 5);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 1;
		gbc_panel_1.gridy = 0;
		pricePane.add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.rowHeights = new int[]{27, 30};
		gbl_panel_1.columnWidths = new int[] {};
		gbl_panel_1.columnWeights = new double[]{1.0};
		gbl_panel_1.rowWeights = new double[]{1.0, 1.0};
		panel_1.setLayout(gbl_panel_1);
		
		JLabel lblSalesTax = new JLabel(" Sales Tax");
		GridBagConstraints gbc_lblSalesTax = new GridBagConstraints();
		gbc_lblSalesTax.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblSalesTax.insets = new Insets(0, 0, 5, 0);
		gbc_lblSalesTax.gridx = 0;
		gbc_lblSalesTax.gridy = 0;
		panel_1.add(lblSalesTax, gbc_lblSalesTax);
		lblSalesTax.setForeground(Color.RED);
		lblSalesTax.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblSalesTax.setHorizontalAlignment(SwingConstants.LEFT);
		
		salesTaxVal = new JLabel("$ 0.00  ");
		GridBagConstraints gbc_salesTaxVal = new GridBagConstraints();
		gbc_salesTaxVal.anchor = GridBagConstraints.EAST;
		gbc_salesTaxVal.gridx = 0;
		gbc_salesTaxVal.gridy = 1;
		panel_1.add(salesTaxVal, gbc_salesTaxVal);
		salesTaxVal.setForeground(Color.RED);
		salesTaxVal.setFont(new Font("Tahoma", Font.BOLD, 30));
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.BLACK);
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 2;
		gbc_panel_2.gridy = 0;
		pricePane.add(panel_2, gbc_panel_2);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.rowHeights = new int[]{21, 0, 0};
		gbl_panel_2.columnWeights = new double[]{1.0};
		gbl_panel_2.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);
		
		JLabel lblGrandTotal = new JLabel(" Total");
		GridBagConstraints gbc_lblGrandTotal = new GridBagConstraints();
		gbc_lblGrandTotal.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblGrandTotal.insets = new Insets(0, 0, 5, 0);
		gbc_lblGrandTotal.gridx = 0;
		gbc_lblGrandTotal.gridy = 0;
		panel_2.add(lblGrandTotal, gbc_lblGrandTotal);
		lblGrandTotal.setForeground(Color.RED);
		lblGrandTotal.setFont(new Font("Tahoma", Font.BOLD, 13));
		
		grandTotalVal = new JLabel("$ 0.00 ");
		GridBagConstraints gbc_grandTotalVal = new GridBagConstraints();
		gbc_grandTotalVal.anchor = GridBagConstraints.EAST;
		gbc_grandTotalVal.gridx = 0;
		gbc_grandTotalVal.gridy = 1;
		panel_2.add(grandTotalVal, gbc_grandTotalVal);
		grandTotalVal.setForeground(Color.RED);
		grandTotalVal.setFont(new Font("Tahoma", Font.BOLD, 30));
		
		JPanel panel_4 = new JPanel();
		panel_3.add(panel_4, BorderLayout.SOUTH);
		GridBagLayout gbl_panel_4 = new GridBagLayout();
		gbl_panel_4.columnWidths = new int[]{275, 0};
		gbl_panel_4.rowHeights = new int[]{46, 0};
		gbl_panel_4.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel_4.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_4.setLayout(gbl_panel_4);
		
		JButton keyF2 = new JButton("F2: Item Lookup");
		keyF2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				itemSearchUI.setVisible(true);
			}
		});
		keyF2.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_keyF2 = new GridBagConstraints();
		gbc_keyF2.fill = GridBagConstraints.BOTH;
		gbc_keyF2.gridx = 0;
		gbc_keyF2.gridy = 0;
		panel_4.add(keyF2, gbc_keyF2);
		
		tenderUI = new TenderUI(this_);
		receiptGenerationUI = new ReceiptGenerationUI();
		invoicePrinter = new InvoicePrintUI();
		itemSearchUI = new ItemSearchUI(this_);
	}
	
	@SuppressWarnings("serial")
	private void initializeItemDataGrid(){
		itemDataGrid = new JTable(){
			public java.awt.Component prepareEditor(javax.swing.table.TableCellEditor editor, int row, int column) {
				if (column == 2 || column == 3) {
					itemDataGrid.setValueAt(null, row, column);
				}
				return super.prepareEditor(editor, row, column);
			};
		};
		
		itemDataGrid.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 13));
		
		Object actionKey = itemDataGrid.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).get(KeyStroke.getKeyStroke("TAB"));
		final Action actionTab = itemDataGrid.getActionMap().get(actionKey);
		itemDataGrid.getActionMap().put(actionKey, new AbstractAction() {

			private static final long serialVersionUID = 8514302304707183748L;

			public void actionPerformed(ActionEvent e) {
				JTable table = (JTable) e.getSource();
				int lastRow = table.getRowCount() - 1;
				int lastColumn = table.getColumnCount() -1;
				if (table.getSelectionModel().getLeadSelectionIndex() == lastRow 
						&& table.getColumnModel().getSelectionModel()
						.getLeadSelectionIndex() == lastColumn) {
					String itemCodeAtLastRow = (String)table.getModel().getValueAt(table.getSelectedRow(), 0);
					if (itemCodeAtLastRow == null || itemCodeAtLastRow.isEmpty()) {
						openTenderWindow();
						return;
					} 
					((DefaultTableModel)table.getModel()).addRow(new Object[]{null, null, null, null, null});
				}
				actionTab.actionPerformed(e);
				if (isInvalidItemCode) {
					table.changeSelection(table.getSelectedRow(), 0, false, false);
					isInvalidItemCode = false;
				}
				deletedRowIdx = -1;
			}
		});
		
		actionKey = itemDataGrid.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).get(KeyStroke.getKeyStroke("ENTER"));
		final Action actionEnter = itemDataGrid.getActionMap().get(actionKey);
		itemDataGrid.getActionMap().put(actionKey, new AbstractAction() {

			private static final long serialVersionUID = -5604206138800741052L;
			
			public void actionPerformed(ActionEvent e) {
				JTable table = (JTable) e.getSource();
				int lastRow = table.getRowCount() - 1, currentRow = table.getSelectionModel().getLeadSelectionIndex();
				boolean isNewRowAdded = false;
				actionEnter.actionPerformed(e);
				if (deletedRowIdx != -1) {
					table.changeSelection(deletedRowIdx, 0, false, false);
					deletedRowIdx = -1;
				}
				if (currentRow == lastRow && !isInvalidItemCode && itemDataGrid.isValid()) {
					String itemCodeAtLastRow = (String)table.getModel().getValueAt(currentRow, 0);
					if (itemCodeAtLastRow == null || itemCodeAtLastRow.isEmpty()) {
						openTenderWindow();
					}
					else{
						((DefaultTableModel)table.getModel()).addRow(new Object[]{null, null, null, null, null});
						lastRow++;
					}
					isNewRowAdded = true;
				}
				if (isNewRowAdded) {
					table.changeSelection(lastRow, 0, false, false);
				}
				isInvalidItemCode = false;
			}
		});
		
		itemDataGrid.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,0), "OnDeletePressed");
		itemDataGrid.getActionMap().put("OnDeletePressed", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				JTable table = (JTable) e.getSource();
				checkAndDeleteRow(table, table.getSelectionModel().getLeadSelectionIndex());
				updateAmounts();
			}
		});

		itemDataGrid.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_F2,0), "F2_PRESSED");
		itemDataGrid.getActionMap().put("F2_PRESSED", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (itemDataGrid.isEditing()) {
					itemDataGrid.getCellEditor().cancelCellEditing();
				}
				itemSearchUI.setVisible(true);
			}
		});
		
		itemDataGrid.setFillsViewportHeight(true);
		itemDataGrid.setFont(new Font("Tahoma", Font.PLAIN, 13));
		itemDataGrid.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null},
			},
			new String[] {
				"Item Lookup Code", "Description", "Quantity", "Price", "Extended"
			}
		) {
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] {
				String.class, String.class, Integer.class, Double.class, Double.class
			};
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				true, false, true, true, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		itemDataGrid.getColumnModel().getColumn(0).setPreferredWidth(140);
		itemDataGrid.getColumnModel().getColumn(1).setPreferredWidth(300);
		itemDataGrid.getColumnModel().getColumn(2).setPreferredWidth(60);
		itemDataGrid.getColumnModel().getColumn(3).setPreferredWidth(60);
		itemDataGrid.getColumnModel().getColumn(4).setPreferredWidth(60);
		
		itemDataGrid.getModel().addTableModelListener(new TableModelListener() {			
			public void tableChanged(TableModelEvent e) {
				if (e.getType() == TableModelEvent.UPDATE) {
					int lastRow = itemDataGrid.getRowCount() - 1, currentRow = e.getFirstRow();
					switch (e.getColumn()) {
					case 0:
						if (StringUtils.isBlank(itemDataGrid.getValueAt(currentRow, e.getColumn()).toString())) {
							if (currentRow != lastRow) {
								checkAndDeleteRow(itemDataGrid, currentRow);
							}
							deletedRowIdx = currentRow;
						}
						else{
							Item item = invoiceGeneratorDAO.getItemByLookupCode(itemDataGrid.getValueAt(currentRow, e.getColumn()).toString());
							if (item != null) {
								itemDataGrid.setValueAt(item.getDescription(), currentRow, 1);
								itemDataGrid.setValueAt(1, currentRow, 2);
								itemDataGrid.setValueAt(item.getPrice(), currentRow, 3);
								invoicedItems.add(item);
							}
							else{
								isInvalidItemCode = true;
								JOptionPane.showMessageDialog(null, "Item lookup code does not exist.");
								checkAndRemoveInvoicedItem(currentRow);
								itemDataGrid.setValueAt("", currentRow, 1);
								itemDataGrid.setValueAt(null, currentRow, 2);
								itemDataGrid.setValueAt(null, currentRow, 3);
								itemDataGrid.setValueAt(null, currentRow, 4);
								itemDataGrid.setValueAt("", currentRow, 0);
							}
						}
						break;
					case 2:
					case 3:
						if (itemDataGrid.getValueAt(currentRow, 2) != null && itemDataGrid.getValueAt(currentRow, 3) != null) {
							itemDataGrid.setValueAt(((Integer)itemDataGrid.getValueAt(currentRow, 2))*(Double)itemDataGrid.getValueAt(currentRow, 3), currentRow, 4);
						}
						else {
							itemDataGrid.setValueAt(null, currentRow, 4);
						}
						break;
					case 4:
						updateAmounts();
						break;
					default:
						break;
					}
					
					
				}
			}
		});
	}
	
	private void checkAndDeleteRow(JTable table, int idx){
		if (table.getRowCount() > 1) {
			checkAndRemoveInvoicedItem(idx);
			((DefaultTableModel)table.getModel()).removeRow(idx);
		}
	}

	public void generateReceipt(Tender tender) {
		tenderUI.closeWindow();
		receiptGenerationUI.showWindow(ia);
		invoiceGeneratorDAO.updateQuantityByItemLookupCode(invoicedItems);
		int isPrintReceipt = JOptionPane.showConfirmDialog(null, "Would you like to print a receipt", "Print Receipt", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
		if (isPrintReceipt == JOptionPane.YES_OPTION) {
			LOG.info("Printing receipt...");
			invoicePrinter.printInvoice((Customer)customerRec.getSelectedItem(), invoicedItems, ia);
		}
		resetData();
		receiptGenerationUI.setTransactionCompleteMsg();
	}
	
	private void openTenderWindow() {
		if (itemDataGrid.getModel().getRowCount() <= 1) {
			JOptionPane.showMessageDialog(null, "Please add atleast 1 product before generating invoice", "", JOptionPane.ERROR_MESSAGE);
		}
		else if(checkGridData()){
			for (int i = 0; i < itemDataGrid.getRowCount()-1; i++) {
				invoicedItems.get(i).setQuantity((Integer)itemDataGrid.getValueAt(i, 2));
				invoicedItems.get(i).setPrice((Double)itemDataGrid.getValueAt(i, 3));
				invoicedItems.get(i).setTotalPrice((Double)itemDataGrid.getValueAt(i, 4));
			}
			tenderUI.showTenderWindow(ia);
		}
	}
	
	private void updateAmounts() {
		InvoiceAmounts ia = calculateInvoiceAmounts();
		subTotalVal.setText(ia.getAmountWithCurency(ia.getSubTotalAmount())+" ");
		salesTaxVal.setText(ia.getAmountWithCurency(ia.getTaxAmount())+" ");
		grandTotalVal.setText(ia.getAmountWithCurency(ia.getGrandTotalAmount())+" ");
	}
	
	private InvoiceAmounts calculateInvoiceAmounts() {
		ia = new InvoiceAmounts();
		int rowCnt = itemDataGrid.getRowCount();
		double subTotal = 0;
		for (int i = 0; i < rowCnt; i++) {
			if (itemDataGrid.getValueAt(i, 4) == null) {
				continue;
			}
			subTotal += (Double)itemDataGrid.getValueAt(i, 4);
		}
		ia.setSubTotalAmount(subTotal);
		ia.setTaxAmount((ia.getSubTotalAmount()*InvoiceGeneratorConfigs.getSalesTax())/100);
		ia.setGrandTotalAmount(ia.getSubTotalAmount()+ia.getTaxAmount());
		return ia;
	}
	
	private void checkAndRemoveInvoicedItem(int rowIdx) {
		if (StringUtils.isNotBlank((String)itemDataGrid.getValueAt(rowIdx, 1))) {
			invoicedItems.remove(rowIdx);
		}
	}
	
	private void resetData(){
		customerRec.setSelectedIndex(0);
		int rowCnt = itemDataGrid.getRowCount();
		for (int i = rowCnt-2; i >= 0; i--) {
			checkAndDeleteRow(itemDataGrid, i);
		}
		updateAmounts();
		itemDataGrid.clearSelection();
		customerRec.requestFocus();
	}
	
	private boolean checkGridData() {
		boolean isValid = true;
		for (int i = 0; i < itemDataGrid.getRowCount()-1; i++) {
			if (itemDataGrid.getValueAt(i, 2) == null || itemDataGrid.getValueAt(i, 3) == null) {
				JOptionPane.showMessageDialog(null, "Quantity and price cannot be empty", "", JOptionPane.ERROR_MESSAGE);
				isValid = false;
				break;
			}
		}
		return isValid;
	}

	public InvoiceGeneratorDAO getInvoiceGeneratorDAO() {
		return invoiceGeneratorDAO;
	}
	
	private void setF2KeyImpl(JRootPane rootPane) {
		CommonUtil.setKeyImpl(rootPane, KeyEvent.VK_F2, "F2_KEY", new AbstractAction() {
			
			private static final long serialVersionUID = -4341295874352913152L;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				itemSearchUI.setVisible(true);				
			}
		});
	}
	
	public void addSearchedItem2Grid(String itemlookupcode) {
		itemSearchUI.setVisible(false);
		if (StringUtils.isNotEmpty((String)itemDataGrid.getValueAt(itemDataGrid.getRowCount()-1, 0))) {
			((DefaultTableModel)itemDataGrid.getModel()).addRow(new Object[]{null, null, null, null, null});
		}
		itemDataGrid.setValueAt(itemlookupcode, itemDataGrid.getRowCount()-1, 0);
		itemDataGrid.changeSelection(itemDataGrid.getRowCount()-1, 0, false, false);
	}
}
