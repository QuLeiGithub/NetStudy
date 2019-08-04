package cn.test.nettycodec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TankMsgTest {
    /**
     * 测试解码
     */
    @Test
    void decode() {

        EmbeddedChannel ch = new EmbeddedChannel();
        ch.pipeline().addLast(new TankMsgDecoder());
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(5);
        buf.writeInt(8);
        //测试解码要写进去让程序解码
        ch.writeInbound(buf);
        TankMsg tm = ch.readInbound();
        assertEquals(5, tm.getX());
        assertEquals(8, tm.getY());
    }

    /**
     * 测试编码
     */
    @Test
    void encode() {
        //内嵌的网络管道测试
        EmbeddedChannel ch = new EmbeddedChannel();
        ch.pipeline().addLast(new TankMsgEncoder());
        TankMsg tankMsg = new TankMsg(5, 8);

        //测试编码时是写出去的
        ch.writeOutbound(tankMsg);
        ByteBuf buf = ch.readOutbound();

        int x = buf.readInt();
        int y = buf.readInt();
        assertEquals(5, x);
        assertEquals(8, y);
    }
}