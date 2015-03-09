package connectionTest.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static connectionTest.Config.CLIENT_ID_PREFIX;
import static connectionTest.Config.SERVER_PORT;

public class Client {

    static final Logger logger = LoggerFactory.getLogger("client");

    private final String host;
    private EventLoopGroup group = new NioEventLoopGroup();
    private Channel channel;
    private String clientId;

    public Client(String host) {
        this.host = host;
    }

    void connect(String clientId) {
        ChannelFuture connectFuture = new Bootstrap().group(group)
                .channel(NioSocketChannel.class)
                .handler(new ClientInitializer())
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1_000)
                .connect(host, SERVER_PORT).syncUninterruptibly();
        channel = connectFuture.channel();
        assert connectFuture.isDone();
        if (connectFuture.isSuccess()) {
            logger.info("Client started. Sending clientId: {}", clientId);
            this.clientId = clientId;
            send(CLIENT_ID_PREFIX + clientId);
        }
        else {
            disconnect();
            throw new ClientException("Failed to connect", connectFuture.cause());
        }
    }

    void send(String msg) {
        logger.info("Sending message: {}", msg);
        ChannelFuture sendFuture = channel.writeAndFlush(msg + "\r\n");
        sendFuture.syncUninterruptibly();
        logger.info("sending {}. {}", sendFuture.isSuccess() ? "success" : "failure", sendFuture);
    }

    void disconnect() {
        if (channel != null) {
            channel.close().syncUninterruptibly();
        }
    }

    void reconnect() {
        // ?
    }

    boolean isConnected() {
        if (channel == null) {
            return false;
        }
        return channel.isOpen();
    }

    void terminate() {
        group.shutdownGracefully();
        disconnect();
        logger.info("Client terminated!");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Client >").append(clientId == null ? "UNREGISTERED" : clientId).append("< [");
        if (channel == null) {
            sb.append("NO CHANNEL");
        }
        else {
            sb.append("open: ").append(channel.isOpen()).append(", ")
              .append("active: ").append(channel.isActive());
        }
        sb.append(']');
        return sb.toString();
    }
}
