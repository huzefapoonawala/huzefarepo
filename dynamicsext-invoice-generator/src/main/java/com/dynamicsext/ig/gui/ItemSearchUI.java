package com.dynamicsext.ig.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.HeadlessException;
import java.awt.Point;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridBagLayout;

import javax.swing.JTextField;

import java.awt.GridBagConstraints;

import javax.swing.JButton;

import java.awt.Insets;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang3.StringUtils;

import com.dynamicsext.ig.util.CommonUtil;
import com.dynamicsext.ig.vo.Item;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.ListSelectionModel;

public class ItemSearchUI extends JFrame {

	private static final long serialVersionUID = -7393891199432442728L;

	private JPanel contentPane;
	private JTextField keywordToSearch;
	private JTable itemGrid;
	
	private InvoiceGeneratorClient invoiceGeneratorClient;
	
	private List<Item> searchedItems;
	private JButton searchBtn;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ItemSearchUI frame = new ItemSearchUI();
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
	public ItemSearchUI() {
		CommonUtil.setEscapeImpl(this.getRootPane());
		setTitle("Search Item");
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 658, 513);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] {0, 0};
		gbl_panel.rowHeights = new int[]{0, 0};
		gbl_panel.columnWeights = new double[]{1.0, 0.0};
		gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		keywordToSearch = new JTextField();
		keywordToSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				super.keyReleased(e);
				JTextField tf = (JTextField) e.getSource();
				if (StringUtils.isNotBlank(tf.getText())) {
					searchItemsByKeyword();
				}
			}
		});
		
		GridBagConstraints gbc_keywordToSearch = new GridBagConstraints();
		gbc_keywordToSearch.insets = new Insets(0, 0, 0, 5);
		gbc_keywordToSearch.fill = GridBagConstraints.HORIZONTAL;
		gbc_keywordToSearch.gridx = 0;
		gbc_keywordToSearch.gridy = 0;
		panel.add(keywordToSearch, gbc_keywordToSearch);
		keywordToSearch.setColumns(10);
		keywordToSearch.addActionListener(new AbstractAction() {

			private static final long serialVersionUID = -396694444622257798L;

			@Override
			public void actionPerformed(ActionEvent e) {
				searchBtn.doClick();				
			}
		});
		
		searchBtn = new JButton("Search");
		searchBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (StringUtils.isBlank(keywordToSearch.getText())) {
					JOptionPane.showMessageDialog(null, "Please enter keyword/s to search", "", JOptionPane.ERROR_MESSAGE);
					return;
				}
				searchItemsByKeyword();
				if (searchedItems.isEmpty()) {
					JOptionPane.showMessageDialog(null, "No items found with entered keyword/s", "", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		GridBagConstraints gbc_searchBtn = new GridBagConstraints();
		gbc_searchBtn.gridx = 1;
		gbc_searchBtn.gridy = 0;
		panel.add(searchBtn, gbc_searchBtn);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		itemGrid = new JTable();
		itemGrid.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		itemGrid.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Item Lookup Code", "Description", "Available Quantity", "Price"
			}
		) {
			private static final long serialVersionUID = 1L;
			
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] {
				String.class, String.class, Integer.class, Double.class
			};
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		itemGrid.getColumnModel().getColumn(0).setResizable(false);
		itemGrid.getColumnModel().getColumn(0).setPreferredWidth(146);
		itemGrid.getColumnModel().getColumn(1).setResizable(false);
		itemGrid.getColumnModel().getColumn(1).setPreferredWidth(288);
		itemGrid.getColumnModel().getColumn(2).setResizable(false);
		itemGrid.getColumnModel().getColumn(2).setPreferredWidth(103);
		itemGrid.getColumnModel().getColumn(3).setResizable(false);
		itemGrid.getColumnModel().getColumn(3).setPreferredWidth(87);
		itemGrid.setFillsViewportHeight(true);
		itemGrid.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				JTable table =(JTable) me.getSource();
				Point p = me.getPoint();
				int row = table.rowAtPoint(p);
				if (me.getClickCount() == 2 && itemGrid.getRowCount() > 0) {
					invoiceGeneratorClient.addSearchedItem2Grid(itemGrid.getValueAt(row, 0).toString());
				}
			}
		});
		scrollPane.setViewportView(itemGrid);
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentHidden(ComponentEvent e) {
				super.componentHidden(e);
				if (searchedItems != null) {
					searchedItems.clear();
					refreshItemGrid();
				}
				keywordToSearch.setText(null);
			}
			
			@Override
			public void componentShown(ComponentEvent e) {
				super.componentShown(e);
				keywordToSearch.requestFocus();
			}
		});
	}

	public ItemSearchUI(InvoiceGeneratorClient invoiceGeneratorClient) throws HeadlessException {
		this();
		this.invoiceGeneratorClient = invoiceGeneratorClient;
	}
	
	private void refreshItemGrid() {
		DefaultTableModel model = ((DefaultTableModel)itemGrid.getModel());
		int rows = itemGrid.getRowCount()-1;
		for (int i = rows; i >= 0; i--) {
			model.removeRow(i);
		}
		for (Item i : searchedItems) {
			model.addRow(new Object[]{i.getItemLookupCode(), i.getDescription(), i.getQuantity(), i.getPrice()});
		}
	}
	
	private void searchItemsByKeyword() {
		searchedItems = invoiceGeneratorClient.getInvoiceGeneratorDAO().getItemsByKeyword(keywordToSearch.getText());
		refreshItemGrid();
	}
}
