package cn.test.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Description: netty是基于纯事件的异步处理
 * * @Author:      QuLei
 * @CreateDate: 2019-08-04 11:42
 * @Version: 1.0
 */
public class NettyServer {

    public static void main(String[] args) throws InterruptedException {
        //负责监听事件的线程组创建2个线程
        EventLoopGroup boosGroup = new NioEventLoopGroup(2);
        //负责处理事件的线程组
        EventLoopGroup workGroup = new NioEventLoopGroup(4);
        //sever启动的辅助类
        ServerBootstrap sb = new ServerBootstrap();
        //这两个参数的位置确定两个线程组的类型，第一个为监听线程组，第二个为处理线程组
        sb.group(boosGroup, workGroup);
        sb.channel(NioServerSocketChannel.class);
        //内部处理accept的过程（使用观察者设计模式）这里是处理每一个连接的
        sb.childHandler(new MyChildInitializer());
        ChannelFuture future = sb.bind(8888).sync();
        future.channel().closeFuture().sync();
        workGroup.shutdownGracefully();

    }
}

class MyChildInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new MyChildHandler());
    }
}

class MyChildHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println(buf.toString());
        ctx.writeAndFlush(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();
    }
}
