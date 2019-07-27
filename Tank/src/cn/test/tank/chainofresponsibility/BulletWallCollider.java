package cn.test.tank.chainofresponsibility;

import cn.test.tank.*;

public class BulletWallCollider implements Collider {
    @Override
    public boolean collide(AbstractGameObject go1, AbstractGameObject go2) {
        if(go1 instanceof Bullet && go2 instanceof Wall){
            Bullet b = (Bullet) go1;
            Wall w = (Wall) go2;
            if(b.isLiving()){
                if(b.getRect().intersects(w.getRect())){
                    b.die();
                    TankFrame.INSTANCE.add(new Explode(b.getX(), b.getY()));
                    return false;
                }
            }
        }else if(go1 instanceof Wall && go2 instanceof Bullet){
           return collide(go2,go1);
        }
        return true;
    }
}
