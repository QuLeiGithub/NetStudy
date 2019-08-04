package cn.test.nettychat;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

public class ChatClient {
    private Channel channel = null;

    /**
     * 连接到服务器
     */
    public void connect() {
        EventLoopGroup worker = new NioEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(worker)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //初始化channel即连接的那个channel
                            channel = socketChannel;
                            socketChannel.pipeline().addLast(new MyHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect("localhost", 8888).sync();
            System.out.println("connected to server");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            worker.shutdownGracefully();
        }
    }

    /**
     * 通过channel往服务器端发送数据
     *
     * @param text
     */
    public void send(String text) {
        channel.writeAndFlush(Unpooled.copiedBuffer(text.getBytes(CharsetUtil.UTF_8)));
    }

    public void closeConnection() {
        send("__bye__");
        channel.close();
    }

    static class MyHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf buf = null;
            try {
                buf = (ByteBuf) msg;
                byte[] bytes = new byte[buf.readableBytes()];
                buf.getBytes(buf.readerIndex(), bytes);
                String str = new String(bytes);
                ClientFrame.INSTANCE.updateText(str);
                //查看当前被用了几个引用
                //System.out.println(buf.refCnt());
            } finally {
                //netty的ByteBuf是直接访问的操作系统的内存;Java虚拟机无法操作这片内存区域,
                // 所以这片内存需要自己释放
                if (buf != null) {
                    ReferenceCountUtil.release(buf);
                }
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }

    }
}
