package eda095.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import eda095.game.Avatar;

public class Player {

	private Socket socket;
	private DataOutputStream out;
	private Avatar myAvatar;
	private int point;
	private int death;
	private String name;

	public Player(Socket socket, Avatar myAvatar) {
		this.socket = socket;
		this.myAvatar = myAvatar;
		point = 0;
		death = 0;
		try {
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public DataOutputStream getOutput() {
		return out;
	}

	public Avatar getAvatar() {
		return myAvatar;
	}

	public synchronized void givePoint() {
		point++;
		System.out.println(point);
	}

	public synchronized int getPoints() {
		return point;
	}

	public void setName(String name) {
		synchronized (name) {
			this.name = name;
		}

	}

	public String getName() {
		synchronized (name) {
			return name;
		}

	}

	public synchronized void giveDeath() {
		death++;
	}

	public synchronized int getDeaths() {
		return death;
	}
}
