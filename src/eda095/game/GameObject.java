package eda095.game;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class GameObject extends Rectangle implements Runnable {

	private static final long serialVersionUID = 4L;
	protected GameObject[][] gameObjects;
	protected boolean isSolid;

	public GameObject(int x, int y, int width, int height, boolean isSolid,
			GameObject[][] gameObjects) {
		super(x, y, width, height);
		this.gameObjects = gameObjects;
		this.isSolid = isSolid;
	}

	protected void fillLevelMatrix() {

		for (int j = x; j < x + width; j++) {
			for (int k = y; k < y + height; k++) {
				gameObjects[j][k] = this;
			}
		}
	}

	public abstract void drawGameObject(Graphics g);

	public abstract boolean givesPoint();

	public boolean isSolid() {
		return isSolid;
	}
}
