package cn.test.tank;

import java.awt.*;

/**
 * 爆炸类
 */
public class Explode extends AbstractGameObject{
    private int x,y;
    private   int width,height;
    //话第几步
    private int step = 0;
    private boolean live = true;


    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public Explode(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = ResourceMgr.explodes[0].getWidth();
        this.height =ResourceMgr.explodes[0].getHeight() ;
        new Thread(()->new Audio("audio/explode.wav").play()).start();
    }

    public void paint(Graphics g) {
        if(!isLive())return;
        g.drawImage(ResourceMgr.explodes[step],x,y,null);
        step++;
        if(step >= ResourceMgr.explodes.length)
            die();
    }

    @Override
    public boolean isLiving() {
        return live;
    }

    private void die() {
        live = false;
    }


}
