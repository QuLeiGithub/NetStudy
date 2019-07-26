package cn.test.tank;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		new Thread(()->new Audio("audio/war1.wav").loop()).start();
		while(true) {
			Thread.sleep(25);
			TankFrame.INSTANCE.repaint();
		}
		
	}

}
