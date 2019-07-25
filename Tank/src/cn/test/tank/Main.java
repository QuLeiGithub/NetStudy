package cn.test.tank;

public class Main {

	public static void main(String[] args) throws InterruptedException {

		while(true) {
			Thread.sleep(25);
			TankFrame.INSTANCE.repaint();
		}
		
	}

}
