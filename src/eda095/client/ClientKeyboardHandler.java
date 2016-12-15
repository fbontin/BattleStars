package eda095.client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ClientKeyboardHandler implements KeyListener, Runnable {
	private boolean left, right, up;
	private Client client;
	private int gameSpeed;

	public ClientKeyboardHandler(Client client, int gameSpeed) {
		this.client = client;
		this.gameSpeed = gameSpeed;
		client.addKeyListener(this);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT
				|| e.getKeyCode() == KeyEvent.VK_A) {
			left = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			up = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT
				|| e.getKeyCode() == KeyEvent.VK_D) {
			right = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT
				|| e.getKeyCode() == KeyEvent.VK_A) {
			left = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			up = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT
				|| e.getKeyCode() == KeyEvent.VK_D) {
			right = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void run() {
		while (true) {
			if (up)
				client.moveAvatarUp();
			if (right)
				client.moveAvatarRight();
			if (left)
				client.moveAvatarLeft();
			if (!up)
				client.moveAvatarDown();
			try {
				Thread.sleep(gameSpeed);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}