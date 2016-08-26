package client;

import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import org.jdesktop.swingx.border.DropShadowBorder;
import java.io.IOException;

public class MainView{

	// GUI
	private JButton ok,min,no;
	private JLabel close,connect,disconnect,connectIcon,param;
	private JFrame window;
	private JPanel logs;
	private TrayIcon trayIcon;
	private Color panelColor;

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
		displayNotification();
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
		JScrollPane scroll = new JScrollPane(logs);


		/*ok = new JButton(listCommands[0]);
		min = new JButton(listCommands[1]);
		no = new JButton(listCommands[2]);*/

		close = new JLabel("Close");
		close.setToolTipText("Disconnect DinnerTime");

		connect = new JLabel(client.isConnected() ? "Connected" : "Disconnected");

		ImageIcon gear = getScaledImageIcon(new ImageIcon("data/img/settings.png"),20,20);
  		param = new JLabel(gear);
  		param.setToolTipText("Settings");
  		
  		ImageIcon error = getScaledImageIcon(new ImageIcon("data/img/error.png"),20,20);
  		connectIcon = new JLabel(error);

  		ImageIcon logout = getScaledImageIcon(new ImageIcon("data/img/logout.png"),20,20);
  		disconnect = new JLabel(logout);
  		disconnect.setToolTipText("Disconnect and close DinnerTime");
		
		north.setLayout(new BoxLayout(north,BoxLayout.LINE_AXIS));
		window.setLayout(new BorderLayout());

		north.add(Box.createRigidArea(new Dimension(5,5)));
		north.add(connectIcon);
		north.add(Box.createRigidArea(new Dimension(5,5)));
		north.add(connect);
		north.add(Box.createHorizontalGlue());
		north.add(disconnect);
		north.add(Box.createRigidArea(new Dimension(5,5)));
		north.add(param);
		north.add(Box.createRigidArea(new Dimension(5,5)));

		south.add(close);

		/*logs.add(ok);
		logs.add(min);
		logs.add(no);*/

		Container contentPane = window.getContentPane();
		contentPane.add(north, BorderLayout.NORTH);
		contentPane.add(scroll,BorderLayout.CENTER);
		contentPane.add(south,BorderLayout.SOUTH);
		window.pack();
		window.setLocation((int)width-305, (int)(height-180-taskBarHeight-5));

	}

	private void attachReactions(){
		MainListener controller = new MainListener(this,client);
		close.addMouseListener(controller);
		trayIcon.addActionListener(controller);
	}

	private ImageIcon getScaledImageIcon(ImageIcon srcImg, int w, int h){
    	Image image = srcImg.getImage();
		Image newimg = image.getScaledInstance(w, h, java.awt.Image.SCALE_SMOOTH); 
		return new ImageIcon(newimg);
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

	public JLabel getCloseLabel(){
		return close;
	}

	public TrayIcon getTrayIcon(){
		return trayIcon;
	}

}