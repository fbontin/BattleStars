package eda095.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerClientHandler implements Runnable {

	private Player[] players;
	private Socket socket;
	private int playerid;
	private PointMonitor pm;

	private DataOutputStream out;
	private DataInputStream in;

	private int playeridin;
	private int xin;
	private int yin;
	private int shotX;
	private int shotY;
	private double shotAngle;
	private boolean gameOver;

	public ServerClientHandler(Player player, Player[] players, int playerid,
			PointMonitor pm) {

		try {
			this.players = players;
			this.socket = player.getSocket();
			this.playerid = playerid;
			this.pm = pm;
			this.gameOver = false;

			this.in = new DataInputStream(socket.getInputStream());
			this.out = new DataOutputStream(socket.getOutputStream());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {

			out.writeInt(playerid);
			System.out.println("Player " + playerid + " connected.");
			String name = in.readUTF();
			System.out.println("Player " + playerid + " set name to " + name);
			players[playerid].setName(name);

		} catch (Exception e) {
			e.printStackTrace();
		}

		outerloop: while (true) {
			// Lägger till i en shotlista och kontrollera kollision mellan
			// spelare och skott.

			try {
				playeridin = in.readInt();
				xin = in.readInt();
				yin = in.readInt();
				players[playeridin].getAvatar().updateAvatarCoordinates(xin,
						yin);
				shotX = in.readInt();
				shotY = in.readInt();
				shotAngle = in.readDouble();
				players[playeridin].getAvatar().getShot()
						.updateCoordinates(shotX, shotY, shotAngle);
				pm.handlePoint(playeridin);

				gameOver = pm.gameOver();
				if (!gameOver) {
					for (int i = 0; i < 4; i++) {
						if (players[i] != null) {
							out.writeBoolean(gameOver);
							out.writeInt(i);
							out.writeInt((int) players[i].getAvatar().getX());
							out.writeInt((int) players[i].getAvatar().getY());

							out.writeInt((int) players[i].getAvatar().getShot()
									.getX());
							out.writeInt((int) players[i].getAvatar().getShot()
									.getY());
							out.writeDouble(players[i].getAvatar().getShot()
									.getAngle());
						}
					}
				} else {
					out.writeBoolean(gameOver);
					for (int i = 0; i < 4; i++) {
						if (players[i] != null) {
							out.writeUTF(players[i].getName());
							out.writeInt(players[i].getPoints());
							out.writeInt(players[i].getDeaths());
						} else {
							out.writeUTF(Integer.toString(i));
							out.writeInt(0);
							out.writeInt(0);
						}
					}
					break outerloop;
				}
			} catch (IOException e) {
				players[playerid] = null;
				System.out.println("Player " + playerid + " disconnected.");
				break;
			}
		}
	}
}
