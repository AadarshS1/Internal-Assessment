import java.awt.BorderLayout;
import java.sql.ResultSet;
import java.time.LocalDate;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDate;

import javax.swing.JComboBox;
import static javax.swing.JOptionPane.getRootFrame;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;



public class UserInputDialog extends JDialog {
	private JComboBox dropdown = new JComboBox();
	private JLabel dayLabel = new JLabel("Day field here");
	private JTextField startField = new JTextField("Start Time");
	private JTextField endField = new JTextField("End Time");
	private JTextField eventLabel = new JTextField("Duty Date");
	private EventDateModel model;
	private LocalDate date;

	
	public UserInputDialog(JFrame owner) {
		super(owner, "Create Event", true);
		this.add(eventLabel, BorderLayout.NORTH);
		JPanel panel = new JPanel();
		panel.add(dayLabel);
		panel.add(startField);
		panel.add(endField);
		panel.add(dropdown);

		updateDrop();

		JButton save = new JButton("Save");
		save.addActionListener(event -> {
			Event.Time start = extractTime(startField.getText());
			Event.Time end = extractTime(endField.getText());
			String selectedteacher = "     " + dropdown.getSelectedItem().toString();
			System.out.println(start);
			System.out.println(end);
			Event sampleEvent = new Event(eventLabel.getText().concat(selectedteacher), start, end);

			boolean addResult = model.addEventToDate(date, sampleEvent);
			if (addResult) {
				setVisible(false);
				model.update();
			} else {
				JOptionPane.showMessageDialog(this, "The time for this event conflicts with an "
						+ "existing event");
				setVisible(true);
			}
		});

		panel.add(save);
		this.add(panel, BorderLayout.SOUTH);
		this.pack();
	}
		private void updateDrop(){
			Connection conn = null;
			String url = "jdbc:mysql://127.0.0.1:3306/IA_1"; //Database -> db_java
			String user = "root";
			String pass = "pavithra15";
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				conn = DriverManager.getConnection(url, user, pass);
				System.out.println("Connection Successful");
				PreparedStatement ps = conn.prepareStatement("SELECT * from teacher");
				ResultSet result = ps.executeQuery();
				while(result.next()){
					String name = result.getString("FirstName");
					dropdown.addItem(name);

				}
			} catch (Exception ex) {
				System.out.println("Error" + ex);
			}

		}


	
	public void attachModel(EventDateModel model, LocalDate date){
		this.model = model;
		this.date = date;
		dayLabel.setText(date.toString());
	}
	
	public Event.Time extractTime(String line){
		int colon = line.indexOf(":");
		int hour = Integer.parseInt(line.substring(0, colon));
		int min = Integer.parseInt(line.substring(colon+1,colon+3));
		boolean am = line.substring(colon+3).equalsIgnoreCase("am");
		
		return new Event.Time(hour,min,am);
	}

	
}




