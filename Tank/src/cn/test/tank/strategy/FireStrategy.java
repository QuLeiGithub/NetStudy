package cn.test.tank.strategy;

import cn.test.tank.Player;

import java.io.Serializable;

/**
 * 采用策略模式设计坦克发射子弹的策略
 */
public interface FireStrategy extends Serializable {
    void fire(Player p);
}
