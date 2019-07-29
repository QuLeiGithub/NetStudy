package cn.test.tank.chainofresponsibility;

import cn.test.tank.AbstractGameObject;
import cn.test.tank.Bullet;
import cn.test.tank.Tank;
import cn.test.tank.Wall;
/**
 * @Description:  tank collide with tank
 ** @Author:      QuLei
 * @CreateDate:   2019-07-28 01:17
 * @Version:      1.0
 */
public class TankTankCollider implements Collider {
    @Override
    public boolean collide(AbstractGameObject go1, AbstractGameObject go2) {
        if((go1!= go2) && (go1 instanceof Tank) && (go2 instanceof Tank)){
            Tank t = (Tank) go1;
            Tank t1 = (Tank) go2;
            if(t.isLiving() && t1.isLiving()){
                if(t.getRect().intersects(t1.getRect())){
                    t.back();
                    t1.back();
                }
            }
        }
        return true;
    }
}
