package cn.test.tank;

import java.util.Random;

public enum Group {
    GOOD, BAD;
    private static Random r = new Random();

    public static Group randomGroup() {
        return Group.values()[r.nextInt(Group.values().length)];
    }
}
