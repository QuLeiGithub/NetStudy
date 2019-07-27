package cn.test.tank;

import org.omg.CORBA.PUBLIC_MEMBER;

import java.awt.*;

public class Bullet extends AbstractGameObject{
    private int x,y;
    private Dir dir;
    private static final int SPEED = 6;
    private Group group;
    private boolean live = true;
    public static int WIDTH = ResourceMgr.bulletU.getWidth();
    public static int HEIGHT = ResourceMgr.bulletU.getHeight();
    private Rectangle rect ;

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isLive() {
        return live;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public Bullet(int x, int y, Dir dir, Group group) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
        rect = new Rectangle(x,y,WIDTH,HEIGHT);
    }

    public void paint(Graphics g) {
        switch(dir) {
            case LEFT:
                g.drawImage(ResourceMgr.bulletL , x, y, null);
                break;
            case UP:
                g.drawImage(ResourceMgr.bulletU, x, y, null);
                break;
            case RIGHT:
                g.drawImage(ResourceMgr.bulletR, x, y, null);
                break;
            case DOWN:
                g.drawImage(ResourceMgr.bulletD, x, y, null);
                break;
        }
        move();

    }

    @Override
    public boolean isLiving() {
        return live;
    }

    private void move() {
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
        //update the rect
        rect.x = x;
        rect.y = y;
        boundsCheck();
    }

    /**
     * 子弹边界检测
     */
    private void boundsCheck() {
        if(x < 0 || y < 30 || x > TankFrame.GAME_WIDTH || y > TankFrame.GAME_HEIGHT){
            live = false;
        }
    }

    public void collidesWithTank(Tank tank){

    }

    public void die(){
        this.setLive(false);
    }
}
