package cn.test.tank;

import cn.test.tank.strategy.FireStrategy;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * 玩家坦克
 */
public class Player extends AbstractGameObject {

    private static final int SPEED = 5;
    public static int WIDTH = ResourceMgr.goodTankU.getWidth();
    public static int HEIGHT = ResourceMgr.goodTankU.getHeight();
    private Group group;
    private Rectangle rect;
    private int x, y;
    private boolean bL, bU, bR, bD;
    private int oldX, oldY;

    private Dir dir = Dir.DOWN;

    private boolean moving = false;
    private boolean living = true;
    private FireStrategy fireStrategy;


    public Player(int x, int y, Dir dir, Group group) {
        super();
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
        oldX = x;
        oldY = y;
        rect = new Rectangle(x, y, WIDTH, HEIGHT);
        //init fire strategy by config file
        initFireStrategy();
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
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

    public void setDir(Dir dir) {
        this.dir = dir;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setLiving(boolean living) {
        this.living = living;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean isLiving() {
        return living;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    private void move() {

        if (!moving) {
            return;
        }
        new Thread(() -> new Audio("audio/tank_move.wav")).start();
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
        rect.x = x;
        rect.y = y;

    }

    @Override
    public void paint(Graphics g) {
        if (!isLiving()) {
            return;
        }
        switch (dir) {
            case LEFT:
                g.drawImage(ResourceMgr.goodTankL, x, y, null);
                break;
            case UP:
                g.drawImage(ResourceMgr.goodTankU, x, y, null);
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

    public void die() {
        this.living = false;
    }


    public void fire() {
        if (isLiving()) {
            fireStrategy.fire(this);
        }
    }

    private void initFireStrategy() {
        Class<FireStrategy> clazz = null;
        try {
            //根据不同的策略加载不同的子弹策略
            clazz = (Class<FireStrategy>) Class.forName("cn.test.tank.strategy." + PropertyMgr.get("tankFireStrategy"));
            fireStrategy = clazz.newInstance();
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

        if (!bL && !bU && !bR && !bD) {
            setMoving(false);
        } else {
            setMoving(true);
            if (bL) {
                setDir(Dir.LEFT);
            }
            if (bU) {
                setDir(Dir.UP);
            }
            if (bR) {
                setDir(Dir.RIGHT);
            }
            if (bD) {
                setDir(Dir.DOWN);
            }
        }
    }

    private void boundsCheck() {
        if (x < 0 || y < 30 || x + Tank.WIDTH > TankFrame.GAME_WIDTH || y + Tank.HEIGHT > TankFrame.GAME_HEIGHT) {
            back();
        }
    }

    /**
     * 出界了回去
     */
    public void back() {
        x = oldX;
        y = oldY;
    }
}





