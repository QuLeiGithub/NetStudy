package cn.test.tank;

import java.awt.Graphics;
import java.util.Random;

public class Tank {
	private static final int SPEED = 5;
	public static int WIDTH = ResourceMgr.goodTankU.getWidth();
	public static int HEIGHT = ResourceMgr.goodTankU.getHeight();
	private Group group;


	private Random random = new Random();

	private int x, y;

	private Dir dir = Dir.DOWN;

	private boolean moving = false;
	private boolean living = true;

	
	public Tank(int x, int y, Dir dir, Group group) {
		super();
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.group = group;

	}

	
	public Dir getDir() {
		return dir;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

    public boolean isLiving() {
        return living;
    }

    public void setLiving(boolean living) {
        this.living = living;
    }

    public boolean isMoving() {
		return moving;
	}

	private void move() {
		
		if(!moving) return ;
		
		switch (dir) {
		case LEFT:
			x -= SPEED;
			break;
		case UP:
			y -= SPEED;
			break;
		case RIGHT:
			x += SPEED;
			break;
		case DOWN:
			y += SPEED;
			break;
		}

		
	}


	
	private void randomDir() {
		
		this.dir = Dir.values()[random.nextInt(4)];
	}
	
	public void paint(Graphics g) {
	    if(!isLiving()) return;
		switch(dir) {
		case LEFT:
			g.drawImage(this.group == Group.GOOD ? ResourceMgr.goodTankL : ResourceMgr.badTankL , x, y, null);
			break;
		case UP:
			g.drawImage(this.group == Group.GOOD ? ResourceMgr.goodTankU : ResourceMgr.badTankU, x, y, null);
			break;
		case RIGHT:
			g.drawImage(this.group == Group.GOOD ? ResourceMgr.goodTankR : ResourceMgr.badTankR, x, y, null);
			break;
		case DOWN:
			g.drawImage(this.group == Group.GOOD ? ResourceMgr.goodTankD : ResourceMgr.badTankD, x, y, null);
			break;
		}
	
		move();
	
	}


	public void setDir(Dir dir) {
		this.dir = dir;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void die() {
		this.living = false;
	}


	public void fire() {
		int bX = this.x + Tank.WIDTH/2 - Bullet.WIDTH/2 ;
		int bY = this.y + Tank.HEIGHT/2 - Bullet.HEIGHT/2;
		TankFrame.INSTANCE.add(new Bullet(bX, bY, this.dir, this.group));

	}
}
