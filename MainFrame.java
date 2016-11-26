import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class MainFrame extends JFrame{
	
	public MainFrame(){
		
		String[] indicators = new String[]{"GDP", "GDP per capita", "Consumer Price Indices", "Retail Price Indices",
				"Unemployment", "Inflation", "Deflation", "Investment", "Production Possibility curve",
				"Aggregate Demand", "Aggregate Supply", "Current account balance"};
		
		setSize(400,600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JTextField textfield = new JTextField();
		textfield.setSize(100, 100);
		
		JList<String> list = new JList<>();
		add(list);
		
		textfield.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				search();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				search();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				search();
			}
			
			public void search(){
				
				if(textfield.getText() != null){
					
					DefaultListModel model = new DefaultListModel();
					
					for(String s: indicators){
						
						if(s.contains(textfield.getText())){
							
							model.addElement(s);
							
						}
						
					}
					
					list.setModel(model);
					
				}
				
			}
			
		});
		
		add(textfield, BorderLayout.NORTH);
		
		setVisible(true);
		
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		new MainFrame();

	}

}
