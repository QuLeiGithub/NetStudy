package cn.test.tank.net;

import cn.test.tank.Bullet;
import cn.test.tank.Dir;
import cn.test.tank.Group;
import cn.test.tank.TankFrame;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.*;
import java.util.UUID;

/**
 * @Description: 打出子弹的消息
 * * @Author:      QuLei
 * @CreateDate: 2019-08-07 23:04
 * @Version: 1.0
 */
@Setter
@Getter
@NoArgsConstructor
@ToString
public class BulletNewMsg extends Msg {
    private UUID playerId;
    private UUID uuid;
    private int x, y;
    private Group group;
    private Dir dir;


    public BulletNewMsg(Bullet bullet) {
        this.playerId = bullet.getPlayerId();
        this.uuid = bullet.getUuid();
        this.x = bullet.getX();
        this.y = bullet.getY();
        this.dir = bullet.getDir();
        this.group = bullet.getGroup();
    }

    @Override
    public byte[] toBytes() {
        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        try {
            baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);
            dos.writeLong(playerId.getMostSignificantBits());
            dos.writeLong(playerId.getLeastSignificantBits());
            dos.writeLong(uuid.getMostSignificantBits());
            dos.writeLong(uuid.getLeastSignificantBits());
            dos.writeInt(x);
            dos.writeInt(y);
            dos.writeInt(dir.ordinal());
            dos.writeInt(group.ordinal());
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
            playerId = new UUID(dis.readLong(), dis.readLong());
            uuid = new UUID(dis.readLong(), dis.readLong());
            x = dis.readInt();
            y = dis.readInt();
            dir = Dir.values()[dis.readInt()];
            group = Group.values()[dis.readInt()];
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
        if (playerId.equals(TankFrame.INSTANCE.getGm().getMyTank().getUuid())) {
            return;
        }
        Bullet bullet = new Bullet(x, y, dir, group, playerId);
        bullet.setUuid(uuid);
        TankFrame.INSTANCE.getGm().add(bullet);
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.BulletNew;
    }
}
