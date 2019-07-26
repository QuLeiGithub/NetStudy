package cn.test.tank;

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
    private List<Tank> tanks;
    private List<Bullet> bullets;
    private List<Explode> explodes;

    /**
     * 初始化遊戲對象
     */
    private void initGameObjects() {
        int tankCount =Integer.parseInt(PropertyMgr.get("initTankCount"));
        myTank = new Player(200, 400, Dir.DOWN, Group.GOOD);
        tanks = new ArrayList<>();
        bullets = new ArrayList<>();
        explodes = new ArrayList<>();
        for (int i = 0; i < tankCount; i++) {
            tanks.add(new Tank(100 + 50 * i, 200, Dir.DOWN, Group.BAD));
        }


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

    public void add(Bullet bullet) {
        bullets.add(bullet);
    }
    public void add(Explode explode){
        explodes.add(explode);

    }


    @Override
    public void paint(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.WHITE);
        g.drawString("bullets:" + bullets.size(), 10, 50);
        g.drawString("tanks:" + tanks.size(), 10, 70);
        g.drawString("explodes:" + explodes.size(), 10, 90);
        g.setColor(c);


        myTank.paint(g);
        for (int i = 0; i < tanks.size(); i++) {
            if (!tanks.get(i).isLiving()) {
                tanks.remove(i);
            } else {
                tanks.get(i).paint(g);
            }
        }
        //画子弹
        for (int i = 0; i < bullets.size(); i++) {
            for (int j = 0; j < tanks.size(); j++) {
                bullets.get(i).collidesWithTank(tanks.get(j));
            }

            //判断当子弹越界时删除
            if (!bullets.get(i).isLive()) {
                bullets.remove(i);
            } else {
                //不越界时画
                bullets.get(i).paint(g);
            }
        }
        for (int i = 0; i < explodes.size(); i++) {
            if (!explodes.get(i).isLive()) {
                explodes.remove(i);
            } else {
                explodes.get(i).paint(g);
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
