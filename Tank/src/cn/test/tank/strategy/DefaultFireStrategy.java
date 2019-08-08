package cn.test.tank.strategy;

import cn.test.tank.Bullet;
import cn.test.tank.Player;
import cn.test.tank.TankFrame;
import cn.test.tank.net.BulletNewMsg;
import cn.test.tank.net.Client;

public class DefaultFireStrategy implements FireStrategy {
    @Override
    public void fire(Player p) {
        int bX = p.getX() + Player.WIDTH / 2 - Bullet.WIDTH / 2;
        int bY = p.getY() + Player.HEIGHT / 2 - Bullet.HEIGHT / 2;
        Bullet bullet = new Bullet(bX, bY, p.getDir(), p.getGroup(), p.getUuid());
        TankFrame.INSTANCE.getGm().add(bullet);
        //send a bullet msg to server when a bullet is born
        Client.INSTANCE.send(new BulletNewMsg(bullet));
    }
}
