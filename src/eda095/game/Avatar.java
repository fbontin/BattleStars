package eda095.game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Avatar extends GameObject {

	private static final long serialVersionUID = 2L;
	private int playerid;
	private BufferedImage imgRight;
	private BufferedImage imgLeft;
	private BufferedImage imgUpRight;
	private BufferedImage imgUpLeft;
	private BufferedImage imgUpLeft2;
	private static boolean isSolid = true;
	private static int movementspeed = 2;
	private static int fallspeed = 2;
	private static int jumpheight = 3;
	private boolean movingRight;
	private boolean movingUp;
	private Shot myShot;
	private int animationCounter;
	private int centerName;
	private String playerName;

	public Avatar(int startX, int startY, int playerid,
			GameObject[][] gameObjects, String playerName) {
		super(startX, startY, 1, 1, isSolid, gameObjects);
		this.playerid = playerid;
		centerName = (playerName.length() * 2) - 1;
		this.playerName = playerName;
		try {
			
			imgRight = ImageIO.read(this.getClass().getResourceAsStream("/sprites/characterRight.png"));
			imgLeft = ImageIO.read(this.getClass().getResourceAsStream("/sprites/characterLeft.png"));
			imgUpRight = ImageIO.read(this.getClass().getResourceAsStream(
					"/sprites/characterFlyingRight.png"));
			imgUpLeft = ImageIO
					.read(this.getClass().getResourceAsStream("/sprites/characterFlyingLeft.png"));
			imgUpLeft2 = ImageIO.read(this.getClass().getResourceAsStream(
					"/sprites/characterFlyingLeft2.png"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		width = imgRight.getWidth();
		height = imgRight.getHeight();
		movingRight = true;
		myShot = new Shot(0, 0, gameObjects, 0);

		fillLevelMatrix();
	}

	public int getPid() {
		return playerid;
	}

	@Override
	public void drawGameObject(Graphics g) {
		if (movingRight) {
			if (movingUp) {
				g.drawImage(imgUpRight, x, y, null);
			} else {
				g.drawImage(imgRight, x, y, null);
			}
		} else {
			if (movingUp) {
				if (animationCounter < 10) {
					g.drawImage(imgUpLeft, x, y, null);
					animationCounter++;
				} else if (animationCounter < 20) {
					g.drawImage(imgUpLeft2, x, y, null);
					animationCounter++;
				} else {
					g.drawImage(imgUpLeft2, x, y, null);
					animationCounter = 0;
				}
			} else {
				g.drawImage(imgLeft, x, y, null);
			}
		}
		g.drawString(playerName, x - centerName, y - 5);
	}

	public void moveLeft() {
		int xDestination = this.x - movementspeed;
		for (int delta = this.x - 1; delta >= xDestination; delta--) {
			for (int i = this.y; i < this.y + this.height - 1; i++) {
				if (this.gameObjects[delta][i].isSolid()) {
					return;
				}
			}
			new AirTile(this.x, this.y, this.width, this.height,
					this.gameObjects);
			movingRight = false;
			x--;
		}
	}

	public void moveRight() {
		int xDestination = this.x + this.width + movementspeed;
		for (int delta = this.x + this.width; delta <= xDestination; delta++) {

			for (int i = this.y; i < this.y + this.height - 1; i++) {
				if (this.gameObjects[delta][i].isSolid) {
					return;
				}
			}
			new AirTile(this.x, this.y, this.width, this.height,
					this.gameObjects);

			x++;
			movingRight = true;
		}
	}

	public void moveUp() {
		int yDestination = this.y - jumpheight;
		myShot.move();

		for (int delta = this.y - 1; delta > yDestination; delta--) {

			for (int i = this.x; i < this.x + this.width; i++) {
				if (this.gameObjects[i][delta].isSolid)
					return;
			}

			new AirTile(this.x, this.y, this.width, this.height,
					this.gameObjects);
			y--;
			movingUp = true;
		}
	}

	public void moveDown() {
		int yDestination = this.y + this.height + fallspeed;
		myShot.move();

		for (int delta = this.y + this.height; delta <= yDestination; delta++) {

			for (int i = this.x; i < this.x + this.width; i++) {
				if (this.gameObjects[i][delta].isSolid)
					return;
			}

			new AirTile(this.x, this.y, this.width, this.height,
					this.gameObjects);
			y++;
			movingUp = false;
		}
	}

	public void updateAvatarCoordinates(int inputX, int inputY) {
		new AirTile(this.x, this.y, this.width, this.height, this.gameObjects);
		if (this.x < inputX) {
			movingRight = true;
		} else if (this.x > inputX) {
			movingRight = false;
		}
		if (this.y < inputY) {
			movingUp = false;
		} else if (this.y > inputY) {
			movingUp = true;
		}
		this.x = inputX;
		this.y = inputY;
		for (int i = x; i < width + x; i++) {
			for (int j = y; j < height + y; j++) {
				gameObjects[i][j] = this;
			}
		}
	}

	public Shot getShot() {
		return myShot;
	}

	public Shot shoot(int mouseX, int mouseY) {
		int dx = this.x - mouseX;
		int dy = this.y - mouseY;
		double angle = Math.atan2((double) dy, (double) dx);
		myShot = new Shot(x, y, gameObjects, angle);
		myShot.setCollided(false);
		return myShot;
	}

	public void enemyShoot(int x, int y, double angle) {
		myShot.updateCoordinates(x, y, angle);
	}

	@Override
	public void run() {
	}

	@Override
	public boolean givesPoint() {
		return true;
	}
}
