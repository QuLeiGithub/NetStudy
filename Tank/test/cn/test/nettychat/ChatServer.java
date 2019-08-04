package cn.test.nettychat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @Description: 聊天室服务端
 * @Author: QuLei
 * @CreateDate: 2019-08-04 12:19
 * @Version: 1.0
 */
public class ChatServer {
    public static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(2);
        EventLoopGroup workerGroup = new NioEventLoopGroup(4);
        ServerBootstrap b = new ServerBootstrap();
        try {
            ChannelFuture future = b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new ServerChildHandler());
                        }
                    }).bind(8888).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
        }
    }

    static class ServerChildHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

            ByteBuf buf = (ByteBuf) msg;
            byte[] bytes = new byte[buf.readableBytes()];
            buf.getBytes(buf.readerIndex(), bytes);
            String str = new String(bytes);
            if ("__bye__".equals(str)) {
                System.out.println("client ready to quit.");
                ChatServer.clients.remove(ctx.channel());
                ctx.close();
                System.out.println(ChatServer.clients.size());
            } else {
                //writeAndFlush会自动释放buf
                ChatServer.clients.writeAndFlush(buf);
            }

        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ChatServer.clients.remove(ctx.channel());
            ctx.close();
        }

        /**
         * @param ctx
         * @throws Exception
         */
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ChatServer.clients.add(ctx.channel());
        }

    }
}
