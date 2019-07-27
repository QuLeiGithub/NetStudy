package cn.test.tank.strategy;

import cn.test.tank.Player;

/**
 * 采用策略模式设计坦克发射子弹的策略
 */
public interface FireStrategy {
    void fire(Player p);
}
