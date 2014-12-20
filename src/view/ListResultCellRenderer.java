package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import model.Result;

public class ListResultCellRenderer extends JLabel implements
		ListCellRenderer<Result> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5291601875494028402L;

	@Override
	public Component getListCellRendererComponent(JList<? extends Result> list,
			Result value, int index, boolean isSelected, boolean cellHasFocus) {
		JPanel cellPanel = new JPanel();
		cellPanel.setLayout(new BorderLayout());
		Result result = (Result) value;
		if (result.getLevel() == 0) {
			cellPanel.setFont(list.getFont());
		} else {
			cellPanel.setFont(cellPanel.getFont().deriveFont(Font.PLAIN));
		}
		
		if (isSelected) {
			cellPanel.setBackground(list.getSelectionBackground());
			cellPanel.setForeground(list.getSelectionForeground());
		} else {
			cellPanel.setBackground(list.getBackground());
			cellPanel.setForeground(list.getForeground());
		}
		
		cellPanel.add(new JLabel(result.getFilename()), BorderLayout.WEST);
		if(result.getLevel() == -1){
			cellPanel.add(new JLabel(result.getQuote()), BorderLayout.EAST);
		}else{
			cellPanel.add(new JLabel("« " + result.getQuote() + " »"), BorderLayout.EAST);
		}
		
		cellPanel.setEnabled(list.isEnabled());
		cellPanel.setOpaque(true);
		return cellPanel;
	}
}