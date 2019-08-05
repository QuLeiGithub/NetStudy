package cn.test.tank;

import cn.test.tank.strategy.FireStrategy;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.UUID;

/**
 * 玩家坦克
 */
@Getter
@Setter
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
    private UUID uuid = UUID.randomUUID();


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


    @Override
    public boolean isLiving() {
        return living;
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

        Color c = g.getColor();
        g.setColor(Color.CYAN);
        g.drawString(uuid.toString(), x, y - 10);
        g.setColor(c);
        switch (dir) {
            case LEFT:
                g.drawImage(group.equals(Group.BAD) ? ResourceMgr.badTankL : ResourceMgr.goodTankL, x, y, null);
                break;
            case UP:
                g.drawImage(group.equals(Group.BAD) ? ResourceMgr.badTankU : ResourceMgr.goodTankU, x, y, null);
                break;
            case RIGHT:
                g.drawImage(group.equals(Group.BAD) ? ResourceMgr.badTankR : ResourceMgr.goodTankR, x, y, null);
                break;
            case DOWN:
                g.drawImage(group.equals(Group.BAD) ? ResourceMgr.badTankD : ResourceMgr.goodTankD, x, y, null);
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





