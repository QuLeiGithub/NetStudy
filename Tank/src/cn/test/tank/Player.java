package cn.test.tank;

import cn.test.tank.strategy.DefaultFireStrategy;
import cn.test.tank.strategy.FireStrategy;
import cn.test.tank.strategy.FourDirFireStrategy;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * 玩家坦克
 */
public class Player {
	private static final int SPEED = 5;
	public static int WIDTH = ResourceMgr.goodTankU.getWidth();
	public static int HEIGHT = ResourceMgr.goodTankU.getHeight();
	private Group group;
	private int x, y;
	private boolean bL, bU, bR, bD;

	private Dir dir = Dir.DOWN;

	private boolean moving = false;
	private boolean living = true;


	public Player(int x, int y, Dir dir, Group group) {
		super();
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.group = group;

	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
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
		new Thread(()->new Audio("audio/tank_move.wav")).start();
		
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

	
	public void paint(Graphics g) {
	    if(!isLiving()) return;
		switch(dir) {
		case LEFT:
			g.drawImage(ResourceMgr.goodTankL , x, y, null);
			break;
		case UP:
			g.drawImage(ResourceMgr.goodTankU , x, y, null);
			break;
		case RIGHT:
			g.drawImage(ResourceMgr.goodTankR, x, y, null);
			break;
		case DOWN:
			g.drawImage(ResourceMgr.goodTankD, x, y, null);
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
		Class<FireStrategy> clazz = null;
		try {
			clazz = (Class<FireStrategy>) Class.forName("cn.test.tank.strategy."+ PropertyMgr.get("tankFireStrategy"));
			FireStrategy fireStrategy = clazz.newInstance();
			fireStrategy.fire(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			switch (key) {
				case KeyEvent.VK_LEFT:
					bL = true;
					break;
				case KeyEvent.VK_UP:
					bU = true;
					break;
				case KeyEvent.VK_RIGHT:
					bR = true;
					break;
				case KeyEvent.VK_DOWN:
					bD = true;
					break;
				default:
					break;

			}
			setMainDir();
		}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
			case KeyEvent.VK_LEFT:
				bL = false;
				break;
			case KeyEvent.VK_UP:
				bU = false;
				break;
			case KeyEvent.VK_RIGHT:
				bR = false;
				break;
			case KeyEvent.VK_DOWN:
				bD = false;
				break;
			case KeyEvent.VK_CONTROL:
				fire();
				break;
		}

		setMainDir();
	}

		private void setMainDir() {

			if (!bL && !bU && !bR && !bD)
				setMoving(false);
			else {
				setMoving(true);
				if (bL)
					setDir(Dir.LEFT);
				if (bU)
					setDir(Dir.UP);
				if (bR)
					setDir(Dir.RIGHT);
				if (bD)
					setDir(Dir.DOWN);
			}
		}
	}





