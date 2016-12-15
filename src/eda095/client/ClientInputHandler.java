package eda095.client;

import java.io.DataInputStream;
import java.io.IOException;

class ClientInputHandler implements Runnable {

	private DataInputStream in;
	private Client client;
	private int xin;
	private int yin;
	private int playerid;
	private int shotX;
	private int shotY;
	private double shotAngle;
	private boolean gameOver;

	public ClientInputHandler(DataInputStream in, Client c) {
		this.in = in;
		this.client = c;
		gameOver = false;
	}

	public void run() {
		boolean run = true;
		while (run) {
			try {
				gameOver = in.readBoolean();
				if (!gameOver) {
					playerid = in.readInt();
					xin = in.readInt();
					yin = in.readInt();

					shotX = in.readInt();
					shotY = in.readInt();
					shotAngle = in.readDouble();
					client.updateCoordinates(playerid, xin, yin);
					client.createEnemyShot(playerid, shotX, shotY, shotAngle);

				} else {
					run = false;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		int[] scoreTable = new int[4];
		int[] deathTable = new int[4];
		String[] nameTable = new String[4];
		for (int i = 0; i < 4; i++) {
			try {
				nameTable[i] = in.readUTF();
				scoreTable[i] = in.readInt();
				deathTable[i] = in.readInt();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		client.endGame(nameTable, scoreTable, deathTable);
	}
}