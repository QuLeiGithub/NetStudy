package cn.test.tank;

import java.awt.*;
import java.util.Random;

public class Tank extends AbstractGameObject{
	private static final int SPEED = 1;
	public static int WIDTH = ResourceMgr.goodTankU.getWidth();
	public static int HEIGHT = ResourceMgr.goodTankU.getHeight();
	private Group group;
	private Dir dir;
	private Rectangle rect;

	private int x, y;
	private int oldX,oldY;


	private boolean moving = true;
	private boolean living = true;

	
	public Tank(int x, int y,Dir dir,Group group) {
		super();
		this.x = x;
		this.y = y;
		this.group = group;
		this.dir = dir;
		oldX = x;
		oldY = y;

		rect = new Rectangle(x,y,WIDTH,HEIGHT);
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

	@Override
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

		if (!moving) {
			return;
		}
		oldX = x;
		oldY = y;
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
		boundsCheck();
		if(r.nextInt(100)>90){
			randomDir();
			fire();
		}

		rect.x = x;
		rect.y = y;

		
	}

	private void boundsCheck() {
		if(x < 0 || y < 30 || x +Tank.WIDTH > TankFrame.GAME_WIDTH || y + Tank.HEIGHT > TankFrame.GAME_HEIGHT){
			back();
		}
	}

	/**
	 * 出界了回去
	 */
	public void back() {
		x= oldX;
		y = oldY;
	}

	private Random r = new Random();

	private void randomDir() {
	    //values返回一个数组
		this.dir = Dir.randomDir();
	}

	@Override
	public void paint(Graphics g) {
		if (!isLiving()) {
			return;
		}
		switch(dir) {
			case LEFT:
				g.drawImage(ResourceMgr.badTankL , x, y, null);
				break;
			case UP:
				g.drawImage(ResourceMgr.badTankU, x, y, null);
				break;
			case RIGHT:
				g.drawImage(ResourceMgr.badTankR, x, y, null);
				break;
			case DOWN:
				g.drawImage(ResourceMgr.badTankD, x, y, null);
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
		TankFrame.INSTANCE.getGm().add(new Explode(x, y));
	}


	public void fire() {
		int bX = this.x + Tank.WIDTH/2 - Bullet.WIDTH/2 ;
		int bY = this.y + Tank.HEIGHT/2 - Bullet.HEIGHT/2;
		TankFrame.INSTANCE.getGm().add(new Bullet(bX, bY, this.dir, this.group));
	}
}
