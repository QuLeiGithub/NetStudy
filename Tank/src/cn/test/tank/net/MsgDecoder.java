package cn.test.tank.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MsgDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> list) throws Exception {
        //消息头为8字节
        if (buf.readableBytes() < 8) {
            return;
        }
        //标记此处读指针
        buf.markReaderIndex();
        MsgType msgType = MsgType.values()[buf.readInt()];
        int length = buf.readInt();
        //长度不够重新设置读指针
        if (buf.readableBytes() < length) {
            buf.resetReaderIndex();
            return;
        }
        byte[] bytes = new byte[length];
        buf.readBytes(bytes);
        //使用反射创建对象
        Msg msg = (Msg) Class.forName("cn.test.tank.net." + msgType.toString() + "Msg").newInstance();
        msg.parse(bytes);
        list.add(msg);
    }
}
