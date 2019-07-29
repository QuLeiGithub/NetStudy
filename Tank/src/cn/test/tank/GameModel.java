package cn.test.tank;

import cn.test.tank.chainofresponsibility.ColliderChain;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:  专门的管理游戏物体的类  实现model和view的分离
 ** @Author:      QuLei
 * @CreateDate:   2019-07-28 21:30
 * @Version:      1.0
 */
public class GameModel {
    private Player myTank;
    private List<AbstractGameObject> objects;
    private ColliderChain chain;

    public Player getMyTank() {
        return myTank;
    }

    public GameModel() {
        initGameObjects();
    }

    /**
     * 初始化遊戲對象
     */
    private void initGameObjects() {
        chain = new ColliderChain();
        int tankCount =Integer.parseInt(PropertyMgr.get("initTankCount"));
        myTank = new Player(200, 400, Dir.DOWN, Group.GOOD);
        objects = new ArrayList<>();
        for (int i = 0; i < tankCount; i++) {
            objects.add(new Tank(100 + 70 * i, 200, Dir.DOWN, Group.BAD));
        }
        //objects.add(wall);
        objects.add(myTank);

    }

    /**
     * 添加游戏对象
     * @param gameObject
     */
    public void add(AbstractGameObject gameObject){
        objects.add(gameObject);
    }

    public void paint(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.WHITE);
        g.drawString("objects:" + objects.size(), 10, 50);
        g.setColor(c);
        for (int i = 0; i < objects.size();i++){
            AbstractGameObject go1 = objects.get(i);
            if(go1.isLiving()){
                for(int j = 0 ; j < objects.size();j++){
                    AbstractGameObject go2 = objects.get(j);
                    chain.collide(go1,go2);
                }
                go1.paint(g);
            }else {
                objects.remove(go1);
            }

        }

    }

}
