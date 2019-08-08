package cn.test.tank.net;

import cn.test.tank.Bullet;
import cn.test.tank.Tank;
import cn.test.tank.TankFrame;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.*;
import java.util.UUID;

@Setter
@Getter
@ToString
/**
 * @Description: 坦克开始移动消息
 ** @Author: QuLei
 * @CreateDate: 2019-08-06 22:28
 * @Version: 1.0
 */
public class TankDieMsg extends Msg {
    private UUID uuid;
    private UUID bulletId;

    public TankDieMsg() {
    }

    public TankDieMsg(UUID uuid, UUID bulletId) {
        this.uuid = uuid;
        this.bulletId = bulletId;
    }

    @Override
    public byte[] toBytes() {
        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        try {
            baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);
            dos.writeLong(uuid.getMostSignificantBits());
            dos.writeLong(uuid.getLeastSignificantBits());
            dos.writeLong(bulletId.getMostSignificantBits());
            dos.writeLong(bulletId.getLeastSignificantBits());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return baos.toByteArray();
    }

    @Override
    public void parse(byte[] bytes) {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
        try {
            uuid = new UUID(dis.readLong(), dis.readLong());
            bulletId = new UUID(dis.readLong(), dis.readLong());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void handle() {
        Bullet bullet = TankFrame.INSTANCE.getGm().findBulletByUUID(bulletId);
        Tank t = TankFrame.INSTANCE.getGm().findTankByUUID(this.uuid);
        //if this msg is send by myself , do nothing.
        if (this.uuid.equals(TankFrame.INSTANCE.getGm().getMyTank().getUuid())) {
            TankFrame.INSTANCE.getGm().getMyTank().die();
        } else {
            if (t != null) {
                t.die();
                bullet.die();
            }
        }
        //find the tank who send the msg

    }

    @Override
    public MsgType getMsgType() {
        return MsgType.TankDie;
    }
}
