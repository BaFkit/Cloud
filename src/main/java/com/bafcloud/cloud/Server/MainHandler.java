package com.bafcloud.cloud.Server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MainHandler extends ChannelInboundHandlerAdapter {

    private static final List<Channel> channels = new ArrayList<>();
    private final String path;

    private final ActionController actionController;
    private boolean downloadFlag = false;
    private boolean uploadFlag = false;

    public MainHandler(String path) {
        actionController = new ActionController(path);
        this.path = path;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected: " + ctx.channel().remoteAddress());
        channels.add(ctx.channel());
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf byteBuf = (ByteBuf) msg;

        if (!uploadFlag) {
            byte[] bytes = new byte[byteBuf.capacity()];
            for (int i = 0; i < byteBuf.capacity(); i++) {
                bytes[i] = byteBuf.getByte(i);
            }

            String str = new String(bytes, 0, bytes.length, StandardCharsets.UTF_8);
            String[] parts = str.trim().split("\\s+");
            String cmd = parts[0];

            switch (cmd) {
                case ("/list"):
                    str = actionController.list();
                    break;
                case ("/mkdir"):
                    str = actionController.mkdir(path);
                    break;
                default:
                    System.out.println("unknown command");
                    break;
            }

            if (downloadFlag) {
                byte[] bytes1 = actionController.getBytes();
                byteBuf = Unpooled.copiedBuffer(bytes1);
                downloadFlag = false;
                ctx.writeAndFlush(byteBuf);
                byteBuf.clear();
                return;
            }

            msg = Unpooled.copiedBuffer(str.getBytes(StandardCharsets.UTF_8));
            ctx.writeAndFlush(msg);
        }
        byteBuf.clear();
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