package cn.test.tank;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

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
            int key = e.getKeyCode();
            if(key == KeyEvent.VK_S) save();
            else if (key == KeyEvent.VK_Q) load();
            else gm.getMyTank().keyPressed(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            gm.getMyTank().keyReleased(e);
        }


        private void save(){
            ObjectOutputStream outputStream = null;
            try {
                File f = new File("F:/test/gf.dat");
                FileOutputStream fileOutputStream = new FileOutputStream(f);
                outputStream = new ObjectOutputStream(fileOutputStream);
                outputStream.writeObject(gm);
                outputStream.flush();

            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void load(){
            ObjectInputStream objectInputStream= null;
            try {
                File f = new File("F:/test/gf.dat");
                FileInputStream fileInputStream = new FileInputStream(f);
                objectInputStream = new ObjectInputStream(fileInputStream);
                gm = (GameModel) objectInputStream.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {

                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public GameModel getGm(){
        return gm;
    }


}
