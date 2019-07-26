package cn.test.tank.strategy;

import cn.test.tank.Bullet;
import cn.test.tank.Dir;
import cn.test.tank.Player;
import cn.test.tank.TankFrame;

/**
 * 坦克四个方向开火
 */
public class FourDirFireStrategy implements FireStrategy{

    @Override
    public void fire(Player p) {
        int bX = p.getX() + Player.WIDTH/2 - Bullet.WIDTH/2 ;
        int bY = p.getY() + Player.HEIGHT/2 - Bullet.HEIGHT/2;
        Dir[] dirs = Dir.values();
        for(Dir d : dirs)
            TankFrame.INSTANCE.add(new Bullet(bX, bY, d, p.getGroup()));
    }
}
