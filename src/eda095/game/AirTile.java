package eda095.game;

import java.awt.Graphics;

public class AirTile extends GameObject {

	private static final long serialVersionUID = 1L;
	private static boolean isSolid = false;

	public AirTile(int x, int y, int width, int height,
			GameObject[][] gameObjects) {
		super(x, y, width, height, isSolid, gameObjects);
		fillLevelMatrix();
	}

	@Override
	public void drawGameObject(Graphics g) {
	}

	@Override
	public void run() {
	}

	@Override
	public boolean givesPoint() {
		return false;
	}
}
