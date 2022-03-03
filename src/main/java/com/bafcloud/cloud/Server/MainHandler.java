package com.bafcloud.cloud.Server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MainHandler extends SimpleChannelInboundHandler <String> {

    private static final List<Channel> channels = new ArrayList<>();

    private final ActionController actionController;


    public MainHandler(String path) {
        actionController = new ActionController(path);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected: " + ctx.channel().remoteAddress());
        channels.add(ctx.channel());
    }


    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        String[] parts = msg.trim().split("\\s+");
        String cmd = parts[0];

        switch (cmd) {
            case ("/list"):
                msg = actionController.list();
                break;
            case ("/mkdir"):
                msg = actionController.mkdir("test");
                break;
            default:
                System.out.println("Не известная комманда");
                break;
        }
        ctx.writeAndFlush(msg);
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client disconnected: " + ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error(cause.getMessage(), cause);
    }
}