package eda095.game;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Shot extends GameObject {

	private static final long serialVersionUID = 6L;
	private double angle;
	public static int WIDTH = 8;
	public static int HEIGHT = 8;
	public static int SPEED = 8;
	private static boolean ISSOLID = false;
	private double preciseX, preciseY;
	private BufferedImage image;
	private boolean hasCollided;

	public Shot(int x, int y, GameObject[][] gameObjects, double angle) {
		super(x, y, WIDTH, HEIGHT, ISSOLID, gameObjects);
		this.angle = angle;
		this.preciseX = x;
		this.preciseY = y;
		this.hasCollided = true;

		try {
			image = ImageIO.read(new File("sprites/shot.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public double getAngle() {
		return angle;
	}

	@Override
	public void run() {
	}

	@Override
	public void drawGameObject(Graphics g) {
		AffineTransform at = new AffineTransform();
		at.translate(image.getWidth() / 2, image.getHeight() / 2);
		at.rotate(angle - (3 * Math.PI / 4), x, y);
		at.translate(x - width, y - width);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(image, at, null);
	}

	public void setCollided(boolean collided) {
		hasCollided = collided;
	}

	public boolean getCollided() {
		return hasCollided;
	}

	public synchronized void move() {
		if (!hasCollided) {

			if (checkHit() != null) {
				hasCollided = true;
			} else {
				preciseX -= Math.cos(angle) * SPEED;
				preciseY -= Math.sin(angle) * SPEED;
				x = (int) Math.round(preciseX);
				y = (int) Math.round(preciseY);
			}
		}
	}

	public synchronized void updateCoordinates(int inputX, int inputY,
			double angle) {

		this.x = inputX;
		this.y = inputY;
		this.angle = angle;
		int xMax = WIDTH + x;
		int yMax = HEIGHT + y;
		for (int i = x; i < xMax; i++) {
			for (int j = y; j < yMax; j++) {
				gameObjects[i][j] = this;
			}
		}
	}

	public GameObject checkHit() {
		int xMax = (this.x + WIDTH);
		int yMax = (this.y + HEIGHT);
		GameObject temp;
		for (int xCount = this.x; xCount <= xMax; xCount++) {
			if (gameObjects[xCount][this.y].isSolid()
					&& gameObjects[xCount][this.y].intersects(this)) {
				temp = gameObjects[xCount][this.y];
				this.x = 0;
				this.y = 0;
				return temp;

			} else if (gameObjects[xCount][yMax].isSolid()
					&& gameObjects[xCount][yMax].intersects(this)) {
				temp = gameObjects[xCount][yMax];
				this.x = 0;
				this.y = 0;
				return temp;
			}
		}
		for (int yCount = this.y; yCount <= yMax; yCount++) {
			if (gameObjects[this.x][yCount].isSolid()
					&& gameObjects[this.x][yCount].intersects(this)) {
				temp = gameObjects[this.x][yCount];
				this.x = 0;
				this.y = 0;
				return temp;
			} else if (gameObjects[xMax][yCount].isSolid()
					&& gameObjects[xMax][yCount].intersects(this)) {
				temp = gameObjects[xMax][yCount];
				this.x = 0;
				this.y = 0;
				return temp;
			}
		}
		return null;
	}

	@Override
	public boolean givesPoint() {
		return false;
	}
}
