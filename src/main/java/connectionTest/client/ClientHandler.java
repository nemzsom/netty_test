package connectionTest.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import static connectionTest.client.Client.logger;

public class ClientHandler extends SimpleChannelInboundHandler<String> {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        logger.info("Channel registered");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        logger.info("Channel unregistered");
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Channel active");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Channel inactive");
        super.channelInactive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        logger.info("Channel read: {}", msg);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        logger.info("Channel writability changed");
        super.channelWritabilityChanged(ctx);
    }
}
