package cn.test.tank.chainofresponsibility;

import cn.test.tank.AbstractGameObject;
import cn.test.tank.PropertyMgr;

import java.util.ArrayList;
import java.util.List;

public class ColliderChain implements Collider{
    private List<Collider> colliders;
    public ColliderChain(){
        initCollider();
    }
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
