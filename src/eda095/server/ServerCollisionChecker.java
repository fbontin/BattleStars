package eda095.server;

import eda095.game.GameObject;

public class ServerCollisionChecker implements Runnable {
	private Player[] players;
	private int playerid;

	public ServerCollisionChecker(Player[] players, int playerid) {
		this.players = players;
		this.playerid = playerid;
	}

	@Override
	public void run() {

		GameObject gO;

		while (players[playerid] != null) {
			for (int i = 0; i < 4; i++) {
				if (playerid != i) {
					gO = players[i].getAvatar().getShot().checkHit();
					if (gO != null) {
						System.out.println("Someone was hit, probably.");
						gO = null;
					}
				}
			}
		}
	}
}
