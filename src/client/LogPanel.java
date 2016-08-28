package client;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.border.EmptyBorder;
import static common.Constants.getScaledImageIcon;

public class LogPanel extends JPanel{

	protected String message;
	protected JPanel center,south;
	protected JLabel mesLabel,date;
	protected ImageIcon ico;

	public LogPanel(String mes, String path){
		super();
		this.message = mes;
		ico = getScaledImageIcon(path,20,20);

		initGUI();
	}

	private void initGUI(){
		setMinimumSize(new Dimension(272,40));
		setPreferredSize(new Dimension(272,40));
		setMaximumSize(new Dimension(272,40));
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

		center = new JPanel();
		mesLabel = new JLabel(message);
		mesLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		
		center.setLayout(new BoxLayout(center,BoxLayout.LINE_AXIS));
		center.add(Box.createRigidArea(new Dimension(7,0)));
		center.add(new JLabel(c));
		center.add(Box.createRigidArea(new Dimension(7,0)));
		center.add(mesLabel);

		String today = new Date().toString();
		String pattern = "d MMM HH:mm";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		String formatted = formatter.format(new Date());

		date = new JLabel(formatted, JLabel.RIGHT);
		date.setForeground(Color.LIGHT_GRAY);
		date.setFont(new Font("Segoe UI", Font.PLAIN, 10));
		
		south = new JPanel();
		south.setLayout(new BoxLayout(south,BoxLayout.LINE_AXIS));
		south.add(Box.createHorizontalGlue());
		south.add(date);
		south.add(Box.createRigidArea(new Dimension(7,0)));

		add(center, BorderLayout.CENTER);
		add(south, BorderLayout.SOUTH);
	}

}