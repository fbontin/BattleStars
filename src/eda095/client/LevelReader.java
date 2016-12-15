package eda095.client;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;

import java.util.ArrayList;
import java.util.Random;
import eda095.game.AirTile;
import eda095.game.GameObject;
import eda095.game.SolidTile;
import eda095.game.SpawnTile;

public class LevelReader {

	final static int BLOCK_SIZE = 16;

	private ArrayList<SpawnTile> spawnTiles;

	public LevelReader() {
		spawnTiles = new ArrayList<SpawnTile>();
	}

	/**
	 * Creates a level of GameObjects from a picture.
	 * 
	 * @param image
	 *            The image of which the map should be read of.
	 * @param gos
	 *            This method puts all tiles in this matrix of GameObjects.
	 * @param levelTiles
	 *            All new tiles are added to this list.
	 */

	public void readLevel(BufferedImage image, GameObject[][] gos,
			ArrayList<GameObject> levelTiles) {

		int w = image.getWidth(null);
		int h = image.getHeight(null);
		Random rand = new Random();
		String temp;
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				Color c = new Color(image.getRGB(x, y));
				if (c.equals(Color.WHITE)) {
					levelTiles.add(new AirTile(x * BLOCK_SIZE, y * BLOCK_SIZE,
							BLOCK_SIZE, BLOCK_SIZE, gos));
				} else if (c.equals(Color.BLACK)) {
					temp = "GRASS" + (rand.nextInt(3)+1);
					levelTiles.add(new SolidTile(x * BLOCK_SIZE,
							y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, gos,
							temp));
				} else if (c.equals(Color.RED)) {
					temp = "STONE" + (rand.nextInt(6)+1);
					levelTiles.add(new SolidTile(x * BLOCK_SIZE,
							y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, gos,
							temp));
				} else if (c.equals(Color.BLUE)) {
					temp = "WOOD" + (rand.nextInt(5)+1);
					levelTiles.add(new SolidTile(x * BLOCK_SIZE,
							y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, gos,
							temp));
				} else if (c.equals(Color.GREEN)) {
					levelTiles.add(new SolidTile(x * BLOCK_SIZE,
							y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, gos,
							SolidTile.CRAZY));
				} else {
					SpawnTile st = new SpawnTile(x * BLOCK_SIZE,
							y * BLOCK_SIZE, gos);
					levelTiles.add(st);
					spawnTiles.add(st);
				}
			}
		}
	}

	public Point getSpawnPoint() {
		Random r = new Random();
		int i = r.nextInt(spawnTiles.size());
		SpawnTile st = spawnTiles.get(i);
		return new Point(st.x, st.y);
	}
}
