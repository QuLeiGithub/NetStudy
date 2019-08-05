package cn.test.tank;

import cn.test.tank.net.Client;

public class Main {

	public static void main(String[] args) {
		//new Thread(()->new Audio("audio/war1.wav").loop()).start();
		new Thread(() -> {
			while (true) {
				try {
					Thread.sleep(25);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				TankFrame.INSTANCE.repaint();
			}
		}).start();

		Client.INSTANCE.connect();
	}

}
