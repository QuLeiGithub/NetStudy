package cn.test.tank.chainofresponsibility;

import cn.test.tank.AbstractGameObject;
import cn.test.tank.Bullet;
import cn.test.tank.Player;
import cn.test.tank.Tank;

import java.awt.*;

/**
 * @Description: * @Author:      QuLei
 * @CreateDate: 2019-08-03 14:49
 * @Version: 1.0
 */
public class BulletPlayerCollider implements Collider {
    @Override
    public boolean collide(AbstractGameObject go1, AbstractGameObject go2) {
        if (go1 instanceof Bullet && go2 instanceof Player) {
            Bullet b = (Bullet) go1;
            Player p = (Player) go2;
            if (!b.isLiving() || !p.isLiving()) {
                return false;
            }
            if (b.getGroup() == p.getGroup()) {
                return true;
            }

            Rectangle rectTank = p.getRect();
            if (b.getRect().intersects(rectTank)) {
                b.die();
                p.die();
                return false;
            }
        } else if (go1 instanceof Tank && go2 instanceof Bullet) {
            return collide(go2, go1);
        }
        return true;
    }

}

