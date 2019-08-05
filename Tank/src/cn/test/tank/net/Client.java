package cn.test.tank.net;

import cn.test.tank.TankFrame;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @Description: 坦克的客户端
 * * @Author:      QuLei
 * @CreateDate: 2019-08-04 23:35
 * @Version: 1.0
 */
public class Client {

    public static final Client INSTANCE = new Client();
    private Channel channel = null;

    private Client() {
    }

    /**
     * 连接到服务器
     */
    public void connect() {
        EventLoopGroup workerGroup = new NioEventLoopGroup(1);
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            channel = ch;
                            ch.pipeline()
                                    .addLast(new MsgEncoder())
                                    .addLast(new MsgDecoder())
                                    .addLast(new MyHandler());
                        }

                    });
            ChannelFuture future = b.connect("localhost", 8888).sync();
            System.out.println("connected to server.");
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 向服务器发送数据
     *
     * @param msg
     */
    public void send(TankJoinMsg msg) {
        channel.writeAndFlush(msg);
    }

    /**
     * 关闭连接
     */
    public void closeConnection() {
        channel.close();
    }


    class MyHandler extends SimpleChannelInboundHandler<TankJoinMsg> {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ctx.writeAndFlush(new TankJoinMsg(TankFrame.INSTANCE.getGm().getMyTank()));
        }

       /* @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf buf = null;
            try {
                buf = (ByteBuf) msg;
                byte[] bytes = new byte[buf.readableBytes()];
                buf.getBytes(buf.readerIndex(), bytes);
                String str = new String(bytes);
                System.out.println(str);
                //查看当前被用了几个引用
                //System.out.println(buf.refCnt());
            } finally {
                //netty的ByteBuf是直接访问的操作系统的内存;Java虚拟机无法操作这片内存区域,
                // 所以这片内存需要自己释放
                if (buf != null) {
                    ReferenceCountUtil.release(buf);
                }
            }
        }*/

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, TankJoinMsg msg) throws Exception {
            msg.handle();
            System.out.println(msg);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }
}
