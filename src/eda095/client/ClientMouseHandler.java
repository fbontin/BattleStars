package eda095.client;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ClientMouseHandler implements MouseListener, Runnable {
	private Client c;

	public ClientMouseHandler(Client c) {
		this.c = c;
		c.addMouseListener(this);
	}

	@Override
	public void run() {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent me) {
		c.createMyShot(me.getX(), me.getY());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}
