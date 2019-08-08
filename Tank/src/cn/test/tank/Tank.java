package cn.test.tank;

import cn.test.tank.net.TankJoinMsg;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.Random;
import java.util.UUID;

/**
 * 坦克类
 */
@Setter
@Getter
public class Tank extends AbstractGameObject {
    private static final int SPEED = 3;
    public static int WIDTH = ResourceMgr.goodTankU.getWidth();
    public static int HEIGHT = ResourceMgr.goodTankU.getHeight();
    private Group group;
    private Dir dir;
    private Rectangle rect;
    private int x, y;
    private int oldX, oldY;
    private boolean moving = true;
    private boolean living = true;
    private Random r = new Random();
    private UUID uuid;

    public Tank(int x, int y, Dir dir, Group group) {
        super();
        this.x = x;
        this.y = y;
        this.group = group;
        this.dir = dir;
        oldX = x;
        oldY = y;
        rect = new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public Tank(TankJoinMsg msg) {
        this.x = msg.getX();
        this.y = msg.getY();
        this.dir = msg.getDir();
        this.moving = msg.isMoving();
        this.group = msg.getGroup();
        this.uuid = msg.getUuid();
        oldX = x;
        oldY = y;
        rect = new Rectangle(x, y, WIDTH, HEIGHT);
    }


    @Override
    public boolean isLiving() {
        return living;
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
       /* if (r.nextInt(100) > 90) {
            randomDir();
            fire();
        }*/

        rect.x = x;
        rect.y = y;


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

    private void randomDir() {
        //values返回一个数组
        this.dir = Dir.randomDir();
    }

    @Override
    public void paint(Graphics g) {
        if (!isLiving()) {
            return;
        }
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
        TankFrame.INSTANCE.getGm().add(new Explode(x, y));
    }


    public void fire() {
        int bX = this.x + Tank.WIDTH / 2 - Bullet.WIDTH / 2;
        int bY = this.y + Tank.HEIGHT / 2 - Bullet.HEIGHT / 2;
        TankFrame.INSTANCE.getGm().add(new Bullet(bX, bY, this.dir, this.group, uuid));
    }
}
