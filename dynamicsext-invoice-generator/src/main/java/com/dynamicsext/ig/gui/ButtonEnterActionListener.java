package com.dynamicsext.ig.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

public class ButtonEnterActionListener extends AbstractAction{

	private static final long serialVersionUID = -8997701539485138834L;
	
	private JButton btnInFocus;
	
	public ButtonEnterActionListener(JButton btnInFocus) {
		super();
		this.btnInFocus = btnInFocus;
	}

	public void actionPerformed(ActionEvent e) {
		btnInFocus.doClick();
	}

}
