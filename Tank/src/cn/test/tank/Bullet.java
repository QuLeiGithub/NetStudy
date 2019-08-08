package cn.test.tank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.awt.*;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class Bullet extends AbstractGameObject{
    private int x,y;
    private Dir dir;
    private static final int SPEED = 6;
    private Group group;
    private boolean live = true;
    public static int WIDTH = ResourceMgr.bulletU.getWidth();
    public static int HEIGHT = ResourceMgr.bulletU.getHeight();
    private Rectangle rect ;
    private UUID uuid = UUID.randomUUID();
    private UUID playerId;


    public Bullet(int x, int y, Dir dir, Group group, UUID playerId) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
        rect = new Rectangle(x,y,WIDTH,HEIGHT);
        this.playerId = playerId;
        //Client.INSTANCE.send(new BulletNewMsg(this));
    }

    @Override
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


    public void die(){
        this.setLive(false);
    }

    public UUID getPlayerId() {
        return playerId;
    }
}
