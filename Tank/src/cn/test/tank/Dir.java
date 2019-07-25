package cn.test.tank;

import java.util.Random;

public enum Dir {
	LEFT, UP, RIGHT, DOWN;
    private static Random random = new Random();
	public static Dir randomDir(){
	    return values()[random.nextInt(values().length)];
    }
}
