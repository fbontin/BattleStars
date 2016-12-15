package eda095.server;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

import eda095.client.LevelReader;
import eda095.game.Avatar;
import eda095.game.GameObject;

public class Server {

	private static String MAP = "/maps/symmetri2.png";
	private GameObject[][] gameObjects;
	private ArrayList<GameObject> levelTiles;
	private LevelReader lr;
	private Avatar[] avatars;
	private static Player[] players = new Player[4];
	static ServerSocket serverSocket;
	private ExecutorService executors;
	private int tileSize = 16;

	public Server() {

		BufferedImage image = null;
		try {
			System.out.println("Server IP: " + InetAddress.getLocalHost());
			InputStream is = this.getClass().getResourceAsStream(MAP);
			image = ImageIO.read(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int width = image.getWidth(null) * 16;
		int height = image.getHeight(null) * 16;
		gameObjects = new GameObject[width][height];
		levelTiles = new ArrayList<GameObject>();
		lr = new LevelReader();
		lr.readLevel(image, gameObjects, levelTiles);
		avatars = new Avatar[4];
		executors = Executors.newFixedThreadPool(4);

		try {
			serverSocket = new ServerSocket(30000);
			System.out.println("Server started.");
			startServer();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void startServer() {
		PointMonitor pm = new PointMonitor(players);
		while (true) {
			try {
				Socket client = serverSocket.accept();
				for (int i = 0; i < 4; i++) {
					if (players[i] == null) {
						Player player = new Player(client, createAvatar(i));
						players[i] = player;
						ServerClientHandler serverClientHandler = new ServerClientHandler(
								player, players, i, pm);
						executors.submit(serverClientHandler);
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Avatar createAvatar(int playerid) {
		Point p = lr.getSpawnPoint();
		avatars[playerid] = new Avatar(p.x, p.y - tileSize, playerid,
				gameObjects, Integer.toString(playerid));
		return avatars[playerid];
	}

	public static void main(String[] args) {
		new Server();
	}
}