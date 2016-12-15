package eda095.game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SolidTile extends GameObject {
	private static final long serialVersionUID = 7L;
	private static boolean isSolid = true;

	public static final String GRASS1 = "GRASS1";
	public static final String GRASS2 = "GRASS2";
	public static final String GRASS3 = "GRASS3";

	public static final String BRICK1 = "BRICK1";
	public static final String BRICK2 = "BRICK2";
	public static final String BRICK3 = "BRICK3";
	public static final String BRICK4 = "BRICK4";

	public static final String METAL1 = "METAL1";
	public static final String METAL2 = "METAL2";
	public static final String METAL3 = "METAL3";
	public static final String METALBOX = "METALBOX";

	public static final String STONE1 = "STONE1";
	public static final String STONE2 = "STONE2";
	public static final String STONE3 = "STONE3";
	public static final String STONE4 = "STONE4";
	public static final String STONE5 = "STONE5";
	public static final String STONE6 = "STONE6";
	public static final String STONELEFT = "STONELEFT";
	public static final String STONERIGHT = "STONERIGHT";

	public static final String WOOD1 = "WOOD1";
	public static final String WOOD2 = "WOOD2";
	public static final String WOOD3 = "WOOD3";
	public static final String WOOD4 = "WOOD4";
	public static final String WOOD5 = "WOOD5";
	public static final String WOODBOX = "WOODBOX";
	public static final String CRAZY = "CRAZY";

	private BufferedImage image;

	public SolidTile(int x, int y, int width, int height,
			GameObject[][] gameObjects, String tileType) {

		super(x, y, width, height, isSolid, gameObjects);
		String tileName;

		switch (tileType) {
		case GRASS1:
			tileName = "/tiles/grass1.png";
			break;
		case GRASS2:
			tileName = "/tiles/grass2.png";
			break;
		case GRASS3:
			tileName = "/tiles/grass3.png";
			break;
		case BRICK1:
			tileName = "/tiles/brick1.png";
			break;
		case BRICK2:
			tileName = "/tiles/brick2.png";
			break;
		case BRICK3:
			tileName = "/tiles/brick3.png";
			break;
		case BRICK4:
			tileName = "/tiles/brick4.png";
			break;
		case STONE1:
			tileName = "/tiles/stone1.png";
			break;
		case STONE2:
			tileName = "/tiles/stone2.png";
			break;
		case STONE3:
			tileName = "/tiles/stone3.png";
			break;
		case STONE4:
			tileName = "/tiles/stone4.png";
			break;
		case STONE5:
			tileName = "/tiles/stone5.png";
			break;
		case STONE6:
			tileName = "/tiles/stone6.png";
			break;
		case STONELEFT:
			tileName = "/tiles/stoneLeft.png";
			break;
		case STONERIGHT:
			tileName = "/tiles/stoneRight.png";
			break;
		case WOOD1:
			tileName = "/tiles/wood1.png";
			break;
		case WOOD2:
			tileName = "/tiles/wood2.png";
			break;
		case WOOD3:
			tileName = "/tiles/wood3.png";
			break;
		case WOOD4:
			tileName = "/tiles/wood4.png";
			break;
		case WOOD5:
			tileName = "/tiles/wood5.png";
			break;
		case WOODBOX:
			tileName = "/tiles/woodBox.png";
			break;
		case METAL1:
			tileName = "/tiles/metal1.png";
			break;
		case METAL2:
			tileName = "/tiles/metal2.png";
			break;
		case METAL3:
			tileName = "/tiles/metal3.png";
			break;
		case METALBOX:
			tileName = "/tiles/metalBox.png";
			break;
		case CRAZY:
			tileName = "/tiles/dudeTile.png";
			break;
		default:
			tileName = "/tiles/grassTile.png";
			break;
		}
		try {
			image = ImageIO.read(this.getClass().getResourceAsStream(tileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		fillLevelMatrix();
	}

	public void drawGameObject(Graphics g) {
		g.drawImage(image, x, y, null);
	}

	@Override
	public void run() {
	}

	@Override
	public boolean givesPoint() {
		return false;
	}
}
