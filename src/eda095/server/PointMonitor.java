package eda095.server;

import eda095.game.GameObject;

public class PointMonitor {
	private Player[] players;
	private boolean gameOver;
	private GameObject tempObject;
	private static int GAMEOVERPOINTS = 10;

	public PointMonitor(Player[] players) {
		this.players = players;
		gameOver = false;
	}

	public boolean gameOver() {
		return gameOver;
	}

	public void handlePoint(int playerid) {
		tempObject = players[playerid].getAvatar().getShot().checkHit();
		if (tempObject != null && tempObject != players[playerid].getAvatar()) {
			if (tempObject.givesPoint()) {
				players[playerid].givePoint();
				for (int i = 0; i < 4; i++) {
					if (players[i] != null
							&& tempObject == players[i].getAvatar()) {
						players[i].giveDeath();
					}
				}
				if (players[playerid].getPoints() >= GAMEOVERPOINTS) {
					gameOver = true;
				}
				tempObject = null;
			}
		}
	}
}
