package cn.test.tank.chainofresponsibility;

import cn.test.tank.AbstractGameObject;
import cn.test.tank.PropertyMgr;

import java.util.ArrayList;
import java.util.List;
/**
 * @Description:  处理碰撞的责任链
 ** @Author:      QuLei
 * @CreateDate:   2019-07-28 01:14
 * @Version:      1.0
 */
public class ColliderChain implements Collider {
    private static final long serialVersionUID = -9073837609236975027L;
    private List<Collider> colliders;
    public ColliderChain(){
        initCollider();
    }
    /**
    @Description:  初始化责任链
    * @author      QuLei
    * @return
    * @date        2019-07-28 01:14
    */
    private void initCollider() {
        colliders = new ArrayList<>();
        String [] stringNames = PropertyMgr.get("collidersName").split(",");
        for (String name:stringNames) {
            try {
                Class<Collider> clazz = (Class<Collider>) Class.forName("cn.test.tank.chainofresponsibility."+name);
                colliders.add(clazz.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public boolean collide(AbstractGameObject go1,AbstractGameObject go2){
        for (Collider collider : colliders) {
            if(!collider.collide(go1,go2))
                return false;
        }
        return true;
    }
}
