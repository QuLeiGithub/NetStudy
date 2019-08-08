package cn.test.tank;

import cn.test.tank.chainofresponsibility.ColliderChain;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @Description: 专门的管理游戏物体的类  实现model和view的分离
 * * @Author:      QuLei
 * @CreateDate: 2019-07-28 21:30
 * @Version: 1.0
 */
@Getter
@Setter
public class GameModel implements Serializable {
    private static final long serialVersionUID = 7908205397094452380L;
    private Player myTank;
    private List<AbstractGameObject> objects;
    private ColliderChain chain;
    private Random r = new Random();

    public GameModel() {
        initGameObjects();
    }

    /**
     * 初始化遊戲對象
     */
    private void initGameObjects() {
        chain = new ColliderChain();
        int tankCount = Integer.parseInt(PropertyMgr.get("initTankCount"));

        myTank = new Player(50 + r.nextInt(1000), 50 + r.nextInt(850),
                Dir.randomDir(), Group.randomGroup());
        objects = new ArrayList<>();
        for (int i = 0; i < tankCount; i++) {
            objects.add(new Tank(100 + 70 * i, 200, Dir.DOWN, Group.BAD));
        }
        //objects.add(wall);
        objects.add(myTank);

    }

    /**
     * 添加游戏对象
     *
     * @param gameObject
     */
    public void add(AbstractGameObject gameObject) {
        objects.add(gameObject);
    }

    public void paint(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.WHITE);
        g.drawString("objects:" + objects.size(), 10, 50);
        g.setColor(c);
        for (int i = 0; i < objects.size(); i++) {
            AbstractGameObject object = objects.get(i);
            if (!object.isLiving()) {
                objects.remove(object);
                break;
            }
        }

        for (int i = 0; i < objects.size(); i++) {
            AbstractGameObject go1 = objects.get(i);
            for (int j = 0; j < objects.size(); j++) {
                AbstractGameObject go2 = objects.get(j);
                chain.collide(go1, go2);
            }
            if (go1.isLiving()) {
                go1.paint(g);
            }

        }

    }

    public Tank findTankByUUID(UUID uuid) {
        for (AbstractGameObject o : objects) {
            if (o instanceof Tank) {
                Tank t = (Tank) o;
                if (uuid.equals(t.getUuid())) {
                    return t;
                }
            }
        }
        return null;
    }

    public Bullet findBulletByUUID(UUID bulletId) {
        for (AbstractGameObject b : objects) {
            if (b instanceof Bullet) {
                Bullet bullet = (Bullet) b;
                if (bulletId.equals(bullet.getUuid())) {
                    return bullet;
                }
            }
        }
        return null;
    }
}
