package client;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import java.awt.Color;

import static common.Constants.getCommands;

public class LogEatListener extends MouseAdapter{
	
	private LogEat view;
	private ClientConnexion client;
	private boolean isEnable = true;

	public LogEatListener(LogEat v){
		this.view = v;
		this.client = ClientConnexion.getInstance();
	}

	@Override
	public void mousePressed(MouseEvent e){
		Object src = e.getSource();
		if(isEnable){
			if(src == view.getOk()){
				view.getOk().setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
			}
			else if(src == view.getMin()){
				view.getMin().setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
			}
			else if(src == view.getNo()){
				view.getNo().setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e){
		Object src = e.getSource();
		if(isEnable){
			if(src == view.getOk()){
				view.getOk().setBorder(null);
				client.send(getCommands()[0]);
			}
			else if(src == view.getMin()){
				view.getMin().setBorder(null);
				client.send(getCommands()[1]);
			}
			else if(src == view.getNo()){
				view.getNo().setBorder(null);
				client.send(getCommands()[2]);
			}
			setButtonsState(false);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e){
		Object src = e.getSource();
		if(isEnable){
			if(src == view.getOk()){
				view.getOk().setBackground(Color.WHITE);
			}
			else if(src == view.getMin()){
				view.getMin().setBackground(Color.WHITE);
			}
			else if(src == view.getNo()){
				view.getNo().setBackground(Color.WHITE);
			}
		}
	}

	@Override
	public void mouseExited(MouseEvent e){
		Object src = e.getSource();
		if(isEnable){
			if(src == view.getOk()){
				view.getOk().setBackground(Color.LIGHT_GRAY);
			}
			else if(src == view.getMin()){
				view.getMin().setBackground(Color.LIGHT_GRAY);
			}
			else if(src == view.getNo()){
				view.getNo().setBackground(Color.LIGHT_GRAY);
			}
		}
	}

	private void setButtonsState(boolean enable){
		isEnable = enable;
		view.getOk().setBackground(Color.GRAY);
		view.getMin().setBackground(Color.GRAY);
		view.getNo().setBackground(Color.GRAY);
	}
}