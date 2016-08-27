package client;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


import java.awt.event.AdjustmentListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import static common.Constants.getScaledImageIcon;

public class MainListener extends MouseAdapter implements AdjustmentListener{
	
	MainView view;
	ClientConnexion client;

	public MainListener(MainView v, ClientConnexion c){
		this.view = v;
		this.client = c;
	}

	@Override
	public void mouseClicked(MouseEvent e){
		Object src = e.getSource();
		if(src == view.getSettingsLabel()){
			view.addLog("Test","data/img/clock-1.png");
		}
		else if(src == view.getReloadLabel()){
			view.addLog("Trying to connect","data/img/reload.png");
			new Thread(client).start();
		}
		else if(src == view.getLogoutLabel()){
			System.exit(0);
		}
		else if(src == view.getCloseLabel()){
			view.hideGUI();
		}
		else if(src == view.getTrayIcon()){
			view.displayGUI();
		}
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent e){
		//TODO : Fix because it always change scroll, even when manually changing
        e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
    }

}