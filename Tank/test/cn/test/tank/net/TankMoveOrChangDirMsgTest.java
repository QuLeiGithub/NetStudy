package cn.test.tank.net;

import cn.test.tank.Dir;
import cn.test.tank.Group;
import cn.test.tank.Player;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TankMoveOrChangDirMsgTest {

    @Test
    void encode() {
        EmbeddedChannel channel = new EmbeddedChannel();
        channel.pipeline().addLast(new MsgEncoder());
        Player player = new Player(5, 8, Dir.DOWN, Group.BAD);
        TankMoveOrChangDirMsg msg = new TankMoveOrChangDirMsg(player);
        channel.writeOutbound(msg);
        ByteBuf buf = channel.readOutbound();
        MsgType msgType = MsgType.values()[buf.readInt()];
        int length = buf.readInt();
        UUID uuid = new UUID(buf.readLong(), buf.readLong());
        int x = buf.readInt();
        int y = buf.readInt();
        Dir dir = Dir.values()[buf.readInt()];
        assertEquals(MsgType.TankMoveOrChangDir, msgType);
        assertEquals(28, length);
        assertEquals(5, x);
        assertEquals(8, y);
        assertEquals(Dir.DOWN, dir);
        assertEquals(player.getUuid(), uuid);

    }

    @Test
    void decoder() {
        EmbeddedChannel ch = new EmbeddedChannel();
        ch.pipeline().addLast(new MsgDecoder());
        UUID uuid = UUID.randomUUID();
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(MsgType.TankMoveOrChangDir.ordinal());
        buf.writeInt(28);
        buf.writeLong(uuid.getMostSignificantBits());
        buf.writeLong(uuid.getLeastSignificantBits());
        buf.writeInt(5);
        buf.writeInt(8);
        buf.writeInt(Dir.DOWN.ordinal());

        ch.writeInbound(buf);

        TankMoveOrChangDirMsg msg = ch.readInbound();

        assertEquals(uuid, msg.getUuid());
        assertEquals(5, msg.getX());
        assertEquals(8, msg.getY());
        assertEquals(Dir.DOWN, msg.getDir());
    }
}