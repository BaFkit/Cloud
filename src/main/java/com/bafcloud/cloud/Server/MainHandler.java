package com.bafcloud.cloud.Server;

import com.bafcloud.cloud.Server.Services.AuthorizationService;
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
    private final String root;

    private final ActionController actionController;
    private long uploadFileSize;
    private long capacityClient; //rework
    private boolean uploadFlag = false;
    private String msgSend;

    public MainHandler(AuthorizationService authorizationService, String root) {
        actionController = new ActionController(authorizationService, root);
        this.root = root;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected: " + ctx.channel().remoteAddress());
        channels.add(ctx.channel());
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf byteBuf = (ByteBuf) msg;

        byte[] bytes = new byte[byteBuf.capacity()];
        for (int i = 0; i < byteBuf.capacity(); i++) {
            bytes[i] = byteBuf.getByte(i);
        }

        if (!uploadFlag) {
            String str = new String(bytes, 0, bytes.length, StandardCharsets.UTF_8);
            String[] parts = str.trim().split("\\s+");
            String cmd = parts[0];

            switch (cmd) {
                case ("auth"):
                    msgSend = actionController.authorization(parts);
                    break;
                case ("list"):
                    msgSend = actionController.list(parts[1]);
                    break;
                case ("mkdir"):
                    msgSend = actionController.mkdir(parts);
                    break;
                case ("upload"):
                    msgSend = actionController.upload(parts);
                    break;
                case ("waitingSend"):
                    uploadFlag = true;
                    msgSend = actionController.checkCapacity(parts[1]);
                    break;
                default:
                    msgSend = "unknown";
                    System.out.println("unknown command");
                    break;
            }
            msg = Unpooled.copiedBuffer(msgSend.getBytes(StandardCharsets.UTF_8));
            ctx.writeAndFlush(msg);
            byteBuf.clear();
            return;
        }
        if (uploadFlag) {
            System.out.println("Зашли в закрузку файла");
            msgSend = actionController.uploadFile(bytes);
            uploadFlag = false;
            System.out.println("Вышли из закрузки файла");
            msg = Unpooled.copiedBuffer(msgSend.getBytes(StandardCharsets.UTF_8));
            ctx.writeAndFlush(msg);
            byteBuf.clear();
            return;
        }

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