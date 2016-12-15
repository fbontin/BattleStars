package eda095.client;

import java.io.DataOutputStream;

import eda095.game.Avatar;
import eda095.game.Shot;

class ClientOutputHandler implements Runnable {
	private DataOutputStream out;
	private int playerid;
	private Avatar myAvatar;

	public ClientOutputHandler(DataOutputStream out, int playerid,
			Avatar myAvatar) {
		this.out = out;
		this.playerid = playerid;
		this.myAvatar = myAvatar;
	}

	@Override
	public void run() {
		Shot myShot;
		while (true) {
			try {
				out.writeInt(playerid);
				out.writeInt((int) myAvatar.getX());
				out.writeInt((int) myAvatar.getY());

				myShot = myAvatar.getShot();
				out.writeInt((int) myShot.getX());
				out.writeInt((int) myShot.getY());
				out.writeDouble(myShot.getAngle());

			} catch (Exception e) {
			}
			try {
				Thread.sleep(20);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}