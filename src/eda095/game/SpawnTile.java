package eda095.game;

import java.awt.Graphics;

@SuppressWarnings("serial")
public class SpawnTile extends GameObject {

	static int WIDTH = 16;
	static int HEIGHT = 16;
	private static boolean isSolid = false;

	public SpawnTile(int x, int y, GameObject[][] gameObjects) {
		super(x, y, WIDTH, HEIGHT, isSolid, gameObjects);
		fillLevelMatrix();
	}

	@Override
	public void run() {
	}

	@Override
	public void drawGameObject(Graphics g) {
	}

	@Override
	public boolean givesPoint() {
		// TODO Auto-generated method stub
		return false;
	}
}
