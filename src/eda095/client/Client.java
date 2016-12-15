package eda095.client;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import eda095.game.Avatar;
import eda095.game.GameObject;
import eda095.game.Shot;

public class Client extends Applet implements Runnable {

	private static final long serialVersionUID = 3L;
	private static Socket socket;
	private static DataOutputStream out;
	private static DataInputStream in;
	private static String MAP = "/maps/symmetri2.png";
	private int width;
	private int height;
	private static int GAME_SPEED = 10;
	private int playerid;
	private Avatar[] Avatars;
	private Avatar myAvatar;
	private GameObject[][] gameObjects;
	private ArrayList<GameObject> levelTiles;
	private LevelReader lr;
	private AudioClip[] audioClips;

	private int tileSize = 16;

	private Image i;
	private Graphics doubleG;

	boolean left, right, up;

	public void init() {
		loadSounds();
		BufferedImage image = null;
		try {
			InputStream is = this.getClass().getResourceAsStream(MAP);
			image = ImageIO.read(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		width = image.getWidth(null) * tileSize;
		height = image.getHeight(null) * tileSize;

		setSize(width, height);
		gameObjects = new GameObject[width][height];
		levelTiles = new ArrayList<GameObject>();
		int port = 30000;
		InetAddress address;

		lr = new LevelReader();
		lr.readLevel(image, gameObjects, levelTiles);
		this.setFocusable(true);

		try {
			// Read user input.
			String ip = JOptionPane
					.showInputDialog("Enter the IP-address of the server:");
			address = InetAddress.getByName(ip);
			String name = JOptionPane.showInputDialog("Enter your name:");

			// Open connection to the server.
			socket = new Socket(address, port);
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());

			// Get important information from server.
			playerid = in.readInt();
			createAvatars(name);
			myAvatar = Avatars[playerid];
			if (name.length() != 0) {
				out.writeUTF(name);
			} else {
				out.writeUTF(Integer.toString(playerid));
			}

			// Start server handlers.
			ClientInputHandler input = new ClientInputHandler(in, this);
			ClientOutputHandler output = new ClientOutputHandler(out, playerid,
					myAvatar);

			// Create handlers for server communication.
			Thread clientOutputHandler = new Thread(output);
			Thread clientInputHandler = new Thread(input);

			// Create handlers for user input.
			new ClientMouseHandler(this);
			Thread keyBoardInputHandler = new Thread(new ClientKeyboardHandler(
					this, GAME_SPEED));

			// Start all threads.
			clientInputHandler.start();
			keyBoardInputHandler.start();
			clientOutputHandler.start();
			new Thread(this).start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadSounds() {
		audioClips = new AudioClip[10];
		try {
			audioClips[0] = newAudioClip(new URL(this.getCodeBase(),
					"sounds/colt45.wav"));
			audioClips[1] = newAudioClip(new URL(this.getCodeBase(),
					"sounds/hit.wav"));
			audioClips[2] = newAudioClip(new URL(this.getCodeBase(),
					"sounds/chicken.wav"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public void updateCoordinates(int pid, int x2, int y2) {
		if (pid != myAvatar.getPid())
			Avatars[pid].updateAvatarCoordinates(x2, y2);
	}

	@Override
	public void update(Graphics g) {
		if (i == null) {
			i = createImage(this.getSize().width, this.getSize().height);
			doubleG = i.getGraphics();
		}
		doubleG.setColor(getBackground());
		doubleG.fillRect(0, 0, this.getSize().width, this.getSize().height);

		doubleG.setColor(getForeground());
		paint(doubleG);

		g.drawImage(i, 0, 0, this);
	}

	public void paint(Graphics g) {
		for (int i = 0; i < 4; i++) {
			Avatars[i].drawGameObject(g);
			Shot s = Avatars[i].getShot();
			if (s != null) {
				s.drawGameObject(g);
			}
		}
		for (GameObject gO : levelTiles) {
			gO.drawGameObject(g);
		}
	}

	public void run() {
		while (!socket.isClosed()) {
			repaint();

			try {
				Thread.sleep(GAME_SPEED);
			} catch (InterruptedException e) {

			}
		}
	}

	public void moveAvatarRight() {
		myAvatar.moveRight();
	}

	public void moveAvatarLeft() {
		myAvatar.moveLeft();
	}

	public void moveAvatarUp() {
		myAvatar.moveUp();
	}

	public void moveAvatarDown() {
		myAvatar.moveDown();
	}

	public void createAvatars(String name) {
		Avatars = new Avatar[4];
		for (int i = 0; i < 4; i++) {
			Point p = lr.getSpawnPoint();
			if (playerid == i) {
				Avatars[i] = new Avatar(p.x, p.y - tileSize, i, gameObjects,
						name);
			} else {
				Avatars[i] = new Avatar(p.x, p.y - tileSize, i, gameObjects,
						Integer.toString(i));
			}
		}
	}

	public void createMyShot(int mouseX, int mouseY) {
		if (myAvatar.getShot().getCollided()) {
			myAvatar.shoot(mouseX, mouseY);
			audioClips[0].play();
		}
	}

	public void createEnemyShot(int playerid, int shotX, int shotY,
			double shotAngle) {
		if (playerid != myAvatar.getPid()) {
			Avatars[playerid].enemyShoot(shotX, shotY, shotAngle);
		}
	}

	public void endGame(String[] nameTable, int[] scoreTable, int[] deathTable) {
		int winner = 0;
		int highestPoint = 0;
		for (int i = 0; i < 4; i++) {
			if (highestPoint < scoreTable[i]) {
				winner = i;
				highestPoint = scoreTable[i];
			}
		}
		final JOptionPane pane = new JOptionPane("The winner is: "
				+ nameTable[winner] + "!");
		final JDialog d = pane.createDialog(null, "Score board");
		d.setLayout(new GridLayout(2, 1));
		d.setSize(400, 400);
		JPanel scoreBoard = new JPanel();
		scoreBoard.setLayout(new GridLayout(5, 3));
		JTextField name = new JTextField("Name\n");
		name.setHorizontalAlignment(JTextField.CENTER);
		name.setFont(new Font("SansSerif",Font.BOLD,14));;
		scoreBoard.add(name);

		JTextField kills = new JTextField("Kills\n");
		kills.setHorizontalAlignment(JTextField.CENTER);
		kills.setFont(new Font("SansSerif",Font.BOLD,14));;
		scoreBoard.add(kills);

		JTextField deaths = new JTextField("Deaths\n");
		deaths.setHorizontalAlignment(JTextField.CENTER);
		deaths.setFont(new Font("SansSerif",Font.BOLD,14));;
		scoreBoard.add(deaths);

		for (int i = 0; i < 4; i++) {
			JTextField temp = new JTextField(nameTable[i]);
			temp.setHorizontalAlignment(JTextField.CENTER);
			scoreBoard.add(temp);

			JTextField temp2 = new JTextField(Integer.toString(scoreTable[i]));
			temp2.setHorizontalAlignment(JTextField.CENTER);
			scoreBoard.add(temp2);

			JTextField temp3 = new JTextField(Integer.toString(deathTable[i]));
			temp3.setHorizontalAlignment(JTextField.CENTER);
			scoreBoard.add(temp3);
		}

		scoreBoard.setVisible(true);
		d.add(scoreBoard);

		d.setLocation((width / 2), (height / 2));
		d.setVisible(true);
	}
}