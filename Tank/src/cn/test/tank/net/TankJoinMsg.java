package cn.test.tank.net;

import cn.test.tank.*;
import lombok.Getter;
import lombok.ToString;

import java.io.*;
import java.util.UUID;

@Getter
@ToString
public class TankJoinMsg extends Msg {
    private int x, y;
    private Dir dir;
    private boolean moving;
    private Group group;
    private UUID uuid;//self's id

    public TankJoinMsg() {
    }

    public TankJoinMsg(Player player) {
        this.x = player.getX();
        this.y = player.getY();
        this.dir = player.getDir();
        this.moving = player.isMoving();
        this.group = player.getGroup();
        this.uuid = player.getUuid();
    }

    /**
     * @return tank对象对应的字节数组
     */
    @Override
    public byte[] toBytes() {
        byte[] bytes = null;
        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        try {
            baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);
            dos.writeInt(x);
            dos.writeInt(y);
            dos.writeInt(dir.ordinal());
            dos.writeBoolean(moving);
            dos.writeInt(group.ordinal());
            dos.writeLong(uuid.getMostSignificantBits());
            dos.writeLong(uuid.getLeastSignificantBits());
            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bytes;
    }

    /**
     * 解析消息
     *
     * @param bytes
     */
    @Override
    public void parse(byte[] bytes) {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
        try {
            x = dis.readInt();
            y = dis.readInt();
            dir = Dir.values()[dis.readInt()];
            moving = dis.readBoolean();
            group = Group.values()[dis.readInt()];
            uuid = new UUID(dis.readLong(), dis.readLong());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                dis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 处理消息
     */
    @Override
    public void handle() {
        //if this msg's uuid equals my tank's uuid return
        if (this.uuid.equals(TankFrame.INSTANCE.getGm().getMyTank().getUuid())) {
            return;
        }
        Tank t = new Tank(this);
        if (TankFrame.INSTANCE.getGm().findTankByUUID(this.uuid) != null) {
            return;
        }
        TankFrame.INSTANCE.getGm().add(t);
        Client.INSTANCE.send(new TankJoinMsg(TankFrame.INSTANCE.getGm().getMyTank()));
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.TankJoin;
    }
}
