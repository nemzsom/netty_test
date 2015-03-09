package connectionTest.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Map;

import static connectionTest.Config.CLIENT_ID_PREFIX;
import static connectionTest.server.Server.logger;

@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<String> {

    Map<String, Channel> channels;

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
        if (msg.startsWith(CLIENT_ID_PREFIX)) {
            registerChannel(msg.substring(CLIENT_ID_PREFIX.length()));
        }
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        logger.info("Channel writability changed");
        super.channelWritabilityChanged(ctx);
    }

    private void registerChannel(String clientId) {
        logger.info("register client '{}'", clientId);
    }
}
