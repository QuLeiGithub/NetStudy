package cn.test.tank.net;

import cn.test.tank.Dir;
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
public class TankMoveOrChangDirMsg extends Msg {
    private UUID uuid;
    private int x, y;
    private Dir dir;

    public TankMoveOrChangDirMsg() {
    }

    public TankMoveOrChangDirMsg(Player player) {
        uuid = player.getUuid();
        x = player.getX();
        y = player.getY();
        dir = player.getDir();

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
            dos.writeInt(dir.ordinal());
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
            dir = Dir.values()[dis.readInt()];
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
        if (this.uuid.equals(TankFrame.INSTANCE.getGm().getMyTank().getUuid())) {
            return;
        }
        Tank t = TankFrame.INSTANCE.getGm().findTankByUUID(this.uuid);
        if (t != null) {
            t.setMoving(true);
            t.setX(this.x);
            t.setY(this.y);
            t.setDir(this.dir);
        }

    }

    @Override
    public MsgType getMsgType() {
        return MsgType.TankMoveOrChangDir;
    }
}
