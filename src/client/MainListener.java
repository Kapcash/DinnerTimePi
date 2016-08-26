package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainListener extends MouseAdapter implements ActionListener, AdjustmentListener{
	
	MainView view;
	ClientConnexion client;

	public MainListener(MainView v, ClientConnexion c){
		this.view = v;
		this.client = c;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		/*if(src == ok){
			client.send("OK");
			v.hideNotification();
		}
		else if(src == min){
			client.send("2MIN");
			v.hideNotification();
		}
		else if(src == no){
			client.send("NO");
			v.hideNotification();
		}*/
		if(src == view.getTrayIcon()){
			view.displayGUI();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e){
		Object src = e.getSource();
		if(src == view.getSettingsLabel()){
			view.addLog();
		}
		else if(src == view.getReloadLabel()){
			
		}
		else if(src == view.getLogoutLabel()){

		}
		else if(src == view.getCloseLabel()){
			view.hideGUI();
		}
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {  
        e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
    }

}