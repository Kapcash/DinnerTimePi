package client;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import static common.Constants.getScaledImageIcon;
import static common.Constants.getCommands;

public class MainListener extends MouseAdapter{
	
	MainView view;
	ClientConnexion client;

	public MainListener(MainView v){
		this.view = v;
		this.client = ClientConnexion.getInstance();
		if(client == null) System.out.println("NUULLLL");
	}

	@Override
	public void mouseClicked(MouseEvent e){
		Object src = e.getSource();
		if(src == view.getSettingsLabel()){
			view.addLog("There is no settings.","data/img/reload.png");
		}
		else if(src == view.getReloadLabel()){
			if(!client.isConnected()){
				view.addLog("Trying to connect","data/img/reload.png");
				new Thread(client).start();
			}
		}
		else if(src == view.getLogoutLabel()){
			System.out.println("Exiting application");
			System.exit(0);
		}
		else if(src == view.getCloseLabel()){
			view.hideGUI();
		}
		else if(src == view.getTrayIcon()){
			if(!view.isShown()){
				view.displayGUI();
			} else {
				view.hideGUI();
			}
		}
	}
}