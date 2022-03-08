package com.bafcloud.cloud.Server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerApp {

    private static final int PORT = 8189;
    private static final String PATH = "sd";

    public ServerApp() {
        EventLoopGroup auth = new NioEventLoopGroup(1);
        EventLoopGroup workers = new NioEventLoopGroup();
        MainHandler mainHandler = new MainHandler(PATH);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(auth, workers)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(
                                    mainHandler
                            );
                        }
                    });
            ChannelFuture future = bootstrap.bind(PORT).sync();
            System.out.println("Server started");
            future.channel().closeFuture().sync();
            System.out.println("Server finished");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            auth.shutdownGracefully();
            workers.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new ServerApp();
    }

}
