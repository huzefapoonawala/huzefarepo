package com.dynamicsext.ig.util;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

public class CommonUtil {

	
	public static void setEscapeImpl(JRootPane root) {
		setKeyImpl(root, KeyEvent.VK_ESCAPE, "ESCAPE_KEY", null);
	}
	
	public static void setKeyImpl(JRootPane root, int key, String keyImplIdentifier, AbstractAction actionPerformed) {
		final KeyStroke escapeStroke = KeyStroke.getKeyStroke(key, 0); 
		root.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(escapeStroke, keyImplIdentifier);
		root.getActionMap().put(keyImplIdentifier,
				actionPerformed != null ? 
						actionPerformed :
						new AbstractAction() {
							
							private static final long serialVersionUID = -4341295874352913152L;
				
							@Override
							public void actionPerformed(ActionEvent e) {
								//System.out.println("ESCAPE_KEY pressed");				
							}
						}
		);
	}
}
