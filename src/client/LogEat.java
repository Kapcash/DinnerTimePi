package client;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.border.EmptyBorder;
import static common.Constants.getScaledImageIcon;

public class LogEat extends LogPanel{

	JLabel ok,min,no;

	public LogEat(String mes, ImageIcon c){
		super(mes,c);
		
		initGUI();
		attachReactions();
	}

	public void initGUI(){
		ok = new JLabel("Ok",JLabel.CENTER);
		ok.setMaximumSize(new Dimension(40,20));
		ok.setBackground(Color.LIGHT_GRAY);
		ok.setOpaque(true);
		min = new JLabel("2 min",JLabel.CENTER);
		min.setMaximumSize(new Dimension(40,20));
		min.setBackground(Color.LIGHT_GRAY);
		min.setOpaque(true);
		no = new JLabel("No",JLabel.CENTER);
		no.setMaximumSize(new Dimension(40,20));
		no.setBackground(Color.LIGHT_GRAY);
		no.setOpaque(true);

		center.add(Box.createRigidArea(new Dimension(1,0)));
		center.add(ok);
		center.add(Box.createRigidArea(new Dimension(1,0)));
		center.add(min);
		center.add(Box.createRigidArea(new Dimension(1,0)));
		center.add(no);
		center.add(Box.createRigidArea(new Dimension(1,0)));
	}

	public void attachReactions(){
		LogEatListener controller = new LogEatListener(this);
		ok.addMouseListener(controller);
		min.addMouseListener(controller);
		no.addMouseListener(controller);
	}


	public JLabel getOk(){
		return ok;
	}

	public JLabel getMin(){
		return min;
	}

	public JLabel getNo(){
		return no;
	}

}