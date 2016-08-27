package client;

import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import org.jdesktop.swingx.border.DropShadowBorder;
import java.io.IOException;

import static common.Constants.getScaledImageIcon;

public class MainView{

	// GUI
	private JButton ok,min,no;
	private JLabel close,connect,logout,connectIcon,settings,reload;
	private JFrame window;
	private JPanel logs;
	private TrayIcon trayIcon;
	private Color panelColor;
	private JScrollPane scroll;
	private int i=0;

	/**
	 * Dimensions of the screen.
	 */
	private double width, height;
	private int taskBarHeight;

	/**
	 * True if the notification is shown on the screen.
	 */
	private boolean displayed = false;
	private ClientConnexion client;

	public MainView(ClientConnexion c){
		this.client = c;
		createAndInitGUI();
		attachReactions();
	}

	private void createAndInitGUI(){
		// Getting screen dimensions
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = screenSize.getWidth();
		height = screenSize.getHeight();
		Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		taskBarHeight = (int) height - winSize.height;

		//Init SystemTray
		if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported on this device");
            return;
        }
        BufferedImage img = null;
        Image dinnerIcon = null;
		try {
    		dinnerIcon = ImageIO.read(new File("data/img/clock-1.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        trayIcon = new TrayIcon(dinnerIcon, "DinnerTimePi");
        trayIcon.setImageAutoSize(true);
        try {
            SystemTray.getSystemTray().add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }

		/* Init window */
		window = new JFrame();
		window.setUndecorated(true);
		window.getRootPane().setBorder(new DropShadowBorder(Color.BLACK,5,.4f,10,true,true,true,true));
		window.setMinimumSize(new Dimension(300,180));
		window.setPreferredSize(new Dimension(300,180));

		panelColor = new Color(245,245,245);

		JPanel north = new JPanel();
		north.setBackground(panelColor);
		north.setMinimumSize(new Dimension(300,30));
		north.setPreferredSize(new Dimension(300,30));
		north.setMaximumSize(new Dimension(300,30));

		JPanel south = new JPanel();
		south.setBackground(panelColor);
		south.setMaximumSize(new Dimension(300,30));

		logs = new JPanel();
		logs.setLayout(new BoxLayout(logs,BoxLayout.PAGE_AXIS));
		scroll = new JScrollPane(logs);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 

		close = new JLabel("Close");
		close.setToolTipText("Disconnect DinnerTime");

		connect = new JLabel("Trying to connect");

		ImageIcon gear = getScaledImageIcon(new ImageIcon("data/img/settings.png"),20,20);
  		settings = new JLabel(gear);
  		settings.setToolTipText("Settings");

  		ImageIcon loadIcon = getScaledImageIcon(new ImageIcon("data/img/reload.png"),20,20);
  		reload = new JLabel(loadIcon);
  		reload.setToolTipText("Retry to connect to the server");
  		
  		ImageIcon error = getScaledImageIcon(new ImageIcon("data/img/error.png"),20,20);
  		connectIcon = new JLabel(error);

  		ImageIcon disconnect = getScaledImageIcon(new ImageIcon("data/img/logout.png"),20,20);
  		logout = new JLabel(disconnect);
  		logout.setToolTipText("Disconnect and close DinnerTime");
		
		north.setLayout(new BoxLayout(north,BoxLayout.LINE_AXIS));
		window.setLayout(new BorderLayout());

		north.add(Box.createRigidArea(new Dimension(7,7)));
		north.add(connectIcon);
		north.add(Box.createRigidArea(new Dimension(7,7)));
		north.add(connect);
		north.add(Box.createHorizontalGlue());
		north.add(reload);
		north.add(Box.createRigidArea(new Dimension(7,7)));
		north.add(logout);
		north.add(Box.createRigidArea(new Dimension(7,7)));
		north.add(settings);
		north.add(Box.createRigidArea(new Dimension(7,7)));

		south.add(close);

		Container contentPane = window.getContentPane();
		contentPane.add(north, BorderLayout.NORTH);
		contentPane.add(scroll,BorderLayout.CENTER);
		contentPane.add(south,BorderLayout.SOUTH);
		window.pack();
		window.setLocation((int)width-305, (int)(height-180-taskBarHeight-5));

	}

	private void attachReactions(){
		MainListener controller = new MainListener(this,client);
		settings.addMouseListener(controller);
		close.addMouseListener(controller);
		logout.addMouseListener(controller);
		trayIcon.addMouseListener(controller);
		reload.addMouseListener(controller);
		scroll.getVerticalScrollBar().addAdjustmentListener(controller);
	}

	/**
	 * Display the  GUI notification, asking the user for an answer
	 */
	public void displayGUI() {
		if(!displayed){
			displayed = true;
			System.out.println("Notification displayed");
			//playSound(new File(notifSoundPath)); //TODO : Put in other class
			window.setVisible(true);
		}else{
			System.out.println("[ERR] Notification already displayed");
		}
	}

	public void hideGUI(){
		if(displayed){
			System.out.println("Notification hidden");
			window.setVisible(false);
			displayed = false;
		}else{
			System.out.println("[ERR] Notification already hidden");
		}
	}

	public void addLog(String message, String icoPath){
		logs.add(new LogPanel(message, getScaledImageIcon(new ImageIcon(icoPath),20,20)));
		scroll.revalidate();
		displayGUI();
	}

	public void scroll(){
		JScrollBar vertical = scroll.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}

	public void setConnectionState(boolean isConnected){
		if(isConnected){
			connect.setText("Connected");
			connectIcon.setIcon(getScaledImageIcon(new ImageIcon("data/img/ok.png"),20,20));
		}
		else{
			connect.setText("Disconnected");
			connectIcon.setIcon(getScaledImageIcon(new ImageIcon("data/img/error.png"),20,20));
		}
	}

	/* --- Getters --- */

	public JLabel getConnectIcon(){
		return connectIcon;
	}

	public JLabel getConnect(){
		return connect;
	}

	public JLabel getCloseLabel(){
		return close;
	}

	public TrayIcon getTrayIcon(){
		return trayIcon;
	}

	public JLabel getSettingsLabel(){
		return settings;
	}

	public JLabel getLogoutLabel(){
		return logout;
	}

	public JLabel getReloadLabel(){
		return reload;
	}

}