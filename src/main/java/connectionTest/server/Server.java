package connectionTest.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import connectionTest.Config;

public class Server {

    static final Logger logger = LoggerFactory.getLogger("server");

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public void start() {
        logger.info("Starting the server...");

        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
            .childHandler(new ServerInitializer())
            .option(ChannelOption.SO_BACKLOG, 500)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);

        ChannelFuture connectFuture = bootstrap.bind(Config.SERVER_PORT);
        connectFuture.syncUninterruptibly();
        logger.info("Waiting for client connections on port: {}!", Config.SERVER_PORT);
        connectFuture.channel().closeFuture().syncUninterruptibly();
    }

    public void stop() {
        logger.info("Stopping the server ...");
        Future<?> bShutdownFuture = bossGroup.shutdownGracefully();
        Future<?> wShutdownFuture = workerGroup.shutdownGracefully();
        bShutdownFuture.syncUninterruptibly();
        wShutdownFuture.syncUninterruptibly();
        logger.info("Server stopped.");
    }
}
