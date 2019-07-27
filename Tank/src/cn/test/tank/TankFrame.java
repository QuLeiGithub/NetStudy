package cn.test.tank;

import cn.test.tank.chainofresponsibility.BulletTankCollider;
import cn.test.tank.chainofresponsibility.BulletWallCollider;
import cn.test.tank.chainofresponsibility.Collider;
import cn.test.tank.chainofresponsibility.ColliderChain;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class TankFrame extends Frame {
    public static final TankFrame INSTANCE = new TankFrame();
    public static final int GAME_WIDTH = 1080;
    public static final int GAME_HEIGHT = 960;
    private Player myTank;
    private Wall wall;
    private List<AbstractGameObject> objects;
    private ColliderChain chain = new ColliderChain();


    /**
     * 初始化遊戲對象
     */
    private void initGameObjects() {
        int tankCount =Integer.parseInt(PropertyMgr.get("initTankCount"));
        myTank = new Player(200, 400, Dir.DOWN, Group.GOOD);
        wall = new Wall(250,250,300,150);
        objects = new ArrayList<>();
        for (int i = 0; i < tankCount; i++) {
            objects.add(new Tank(100 + 50 * i, 200, Dir.DOWN, Group.BAD));
        }
        objects.add(wall);
        objects.add(myTank);

    }


    //将窗口类设置成为单例的
    private TankFrame() {
        initGameObjects();
        setSize(GAME_WIDTH, GAME_HEIGHT);
        setResizable(false);
        setTitle("tank war");
        setVisible(true);

        this.addKeyListener(new TankKeyListener());
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }

        });
    }


    /**
     * 添加游戏对象
     * @param gameObject
     */
    public void add(AbstractGameObject gameObject){
        objects.add(gameObject);
    }

    @Override
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

    Image offScreenImage = null;

    @Override
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
        }
        Graphics gOffScreen = offScreenImage.getGraphics();
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.BLACK);
        gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        gOffScreen.setColor(c);
        paint(gOffScreen);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    private class TankKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            myTank.keyPressed(e);
        }


        @Override
        public void keyReleased(KeyEvent e) {
            myTank.keyReleased(e);
        }
    }
}
