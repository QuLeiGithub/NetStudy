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

	Tank myTank = new Tank(200, 400, Dir.DOWN,Group.GOOD);
	Tank enemyTank = new Tank(300, 500, Dir.RIGHT,Group.BAD);

	List<Tank> tanks = new ArrayList<>();
	private List<Bullet> bullets;
	private Bullet bullet;
	
	
	static final int GAME_WIDTH = 1080, GAME_HEIGHT = 960;
	//将窗口类设置成为单例的
	private TankFrame() {
		setSize(GAME_WIDTH, GAME_HEIGHT);
		setResizable(false);
		setTitle("tank war");
		setVisible(true);
		//初始化子弹容器
		bullets = new ArrayList<>();
		this.addKeyListener(new MyKeyListener());
		//bullet = new Bullet(100,100,Dir.DOWN,Group.BAD);


		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) { // bjmashibing/tank
				System.exit(0);
			}

		});
	}

	public void add(Bullet bullet){
		bullets.add(bullet);
	}



	@Override
	public void paint(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.WHITE);
		g.drawString("bullets:"+bullets.size(),10,50);
		g.setColor(c);

		myTank.paint(g);
		enemyTank.paint(g);
		//画子弹
		for(int i = 0; i < bullets.size();i++){
			bullets.get(i).collidesWithTank(enemyTank);
		    //判断当子弹越界时删除
		    if(!bullets.get(i).isLive()){
		        bullets.remove(i);
            }else{
		        //不越界时画
                bullets.get(i).paint(g);
            }
		}

	}

	class MyKeyListener extends KeyAdapter {

		boolean bL = false;
		boolean bU = false;
		boolean bR = false;
		boolean bD = false;

		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			switch (key) {
			case KeyEvent.VK_LEFT:
				bL = true;
				break;
			case KeyEvent.VK_UP:
				bU = true;
				break;
			case KeyEvent.VK_RIGHT:
				bR = true;
				break;
			case KeyEvent.VK_DOWN:
				bD = true;
				break;

			default:
				break;
			}

			setMainTankDir();

		}

		@Override
		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();
			switch (key) {
			case KeyEvent.VK_LEFT:
				bL = false;
				break;
			case KeyEvent.VK_UP:
				bU = false;
				break;
			case KeyEvent.VK_RIGHT:
				bR = false;
				break;
			case KeyEvent.VK_DOWN:
				bD = false;
				break;
			case KeyEvent.VK_CONTROL:
				myTank.fire();
				break;

			default:
				break;
			}

			setMainTankDir();
		}

		private void setMainTankDir() {

			if (!bL && !bU && !bR && !bD)
				myTank.setMoving(false);
			else {
				myTank.setMoving(true);

				if (bL)
					myTank.setDir(Dir.LEFT);
				if (bU)
					myTank.setDir(Dir.UP);
				if (bR)
					myTank.setDir(Dir.RIGHT);
				if (bD)
					myTank.setDir(Dir.DOWN);
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
}
