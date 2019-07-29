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
/**
 * @Description:  游戏窗口类
 ** @Author:      QuLei
 * @CreateDate:   2019-07-28 01:15
 * @Version:      1.0
 */
public class TankFrame extends Frame {
    public static final TankFrame INSTANCE = new TankFrame();
    private GameModel gm;
    public static final int GAME_WIDTH = 1080;
    public static final int GAME_HEIGHT = 960;





    //将窗口类设置成为单例的
    private TankFrame() {
        gm = new GameModel();
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




    @Override
    public void paint(Graphics g) {
        gm.paint(g);
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
            gm.getMyTank().keyPressed(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            gm.getMyTank().keyReleased(e);
        }
    }

    public GameModel getGm(){
        return gm;
    }
}
