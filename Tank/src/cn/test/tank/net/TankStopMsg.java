package cn.test.tank.net;

import cn.test.tank.Player;
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
public class TankStopMsg extends Msg {
    private UUID uuid;
    private int x, y;

    public TankStopMsg() {
    }

    public TankStopMsg(Player player) {
        uuid = player.getUuid();
        x = player.getX();
        y = player.getY();

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
            dos.writeInt(x);
            dos.writeInt(y);
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
            x = dis.readInt();
            y = dis.readInt();
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
        //if this msg is send by myself , do nothing.
        if (this.uuid.equals(TankFrame.INSTANCE.getGm().getMyTank().getUuid())) {
            return;
        }
        //find the tank who send the msg
        Tank t = TankFrame.INSTANCE.getGm().findTankByUUID(this.uuid);
        if (t != null) {
            t.setMoving(false);
            t.setX(this.x);
            t.setY(this.y);
        }

    }

    @Override
    public MsgType getMsgType() {
        return MsgType.TankStop;
    }
}
