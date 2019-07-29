package cn.test.tank.chainofresponsibility;

import cn.test.tank.AbstractGameObject;

import java.io.Serializable;

public interface Collider extends Serializable {
    /**
     *
     * @param go1
     * @param go2
     * @return true chain go on ,false chain break
     */
    boolean collide(AbstractGameObject go1,AbstractGameObject go2);
}
