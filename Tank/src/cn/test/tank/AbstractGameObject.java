package cn.test.tank;

import java.awt.*;
import java.io.Serializable;

/**
 * @Description:   游戏物体的抽象类
 ** @Author:      QuLei
 * @CreateDate:   2019-07-27 11:35
 * @Version:      1.0
 */
public abstract class AbstractGameObject implements Serializable {
    private static final long serialVersionUID = -8668511151481388820L;

    public abstract void paint(Graphics g);
    public abstract boolean isLiving();
}
