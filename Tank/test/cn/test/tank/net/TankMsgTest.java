package cn.test.tank.net;

import cn.test.tank.Dir;
import cn.test.tank.Group;
import cn.test.tank.Player;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TankMsgTest {

    @Test
    void encode() {
        EmbeddedChannel channel = new EmbeddedChannel();
        channel.pipeline().addLast(new MsgEncoder());
        Player player = new Player(5, 8, Dir.DOWN, Group.BAD);
        TankJoinMsg tjm = new TankJoinMsg(player);
        channel.writeOutbound(tjm);
        ByteBuf buf = channel.readOutbound();
        MsgType msgType = MsgType.values()[buf.readInt()];
        int length = buf.readInt();
        int x = buf.readInt();
        int y = buf.readInt();
        Dir dir = Dir.values()[buf.readInt()];
        boolean moving = buf.readBoolean();
        Group group = Group.values()[buf.readInt()];
        UUID uuid = new UUID(buf.readLong(), buf.readLong());
        assertEquals(MsgType.TankJoin, msgType);
        assertEquals(33, length);
        assertEquals(5, x);
        assertEquals(8, y);
        assertEquals(Dir.DOWN, dir);
        assertFalse(moving);
        assertEquals(Group.BAD, group);
        assertEquals(player.getUuid(), uuid);

    }

    @Test
    void decoder() {
        EmbeddedChannel ch = new EmbeddedChannel();
        ch.pipeline().addLast(new MsgDecoder());
        UUID uuid = UUID.randomUUID();
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(MsgType.TankJoin.ordinal());
        buf.writeInt(33);
        buf.writeInt(5);
        buf.writeInt(8);
        buf.writeInt(Dir.DOWN.ordinal());
        buf.writeBoolean(true);
        buf.writeInt(Group.GOOD.ordinal());
        buf.writeLong(uuid.getMostSignificantBits());
        buf.writeLong(uuid.getLeastSignificantBits());

        ch.writeInbound(buf);

        TankJoinMsg tjm = ch.readInbound();

        assertEquals(5, tjm.getX());
        assertEquals(8, tjm.getY());
        assertEquals(Dir.DOWN, tjm.getDir());
        assertTrue(tjm.isMoving());
        assertEquals(Group.GOOD, tjm.getGroup());
        assertEquals(uuid, tjm.getUuid());
    }
}