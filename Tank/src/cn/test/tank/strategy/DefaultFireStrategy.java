package cn.test.tank.strategy;

import cn.test.tank.Bullet;
import cn.test.tank.Player;
import cn.test.tank.TankFrame;

public class DefaultFireStrategy implements FireStrategy{
    @Override
    public void fire(Player p) {
        int bX = p.getX() + Player.WIDTH/2 - Bullet.WIDTH/2 ;
        int bY = p.getY() + Player.HEIGHT/2 - Bullet.HEIGHT/2;
        TankFrame.INSTANCE.getGm().add(new Bullet(bX, bY, p.getDir(), p.getGroup()));
    }
}
