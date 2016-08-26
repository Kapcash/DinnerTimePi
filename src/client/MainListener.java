package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainListener extends MouseAdapter implements ActionListener{
	
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
			view.displayNotification();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e){
		Object src = e.getSource();

		view.hideNotification();
	}

}