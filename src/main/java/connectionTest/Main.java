package connectionTest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import connectionTest.client.ClientController;
import connectionTest.server.ServerController;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger("main");

    public static void main(String[] args) {
        if (args.length == 0) {
            logger.error("Not enough argument");
            System.exit(1);
        }
        Runnable controller = null;
        String type = args[0];
        switch (type) {
            case "server":
                controller = new ServerController();
                break;
            case "client":
                String host = args.length > 1 ? args[1] : Config.DEF_SERVER_HOST;
                controller = new ClientController(host);
                break;
            default:
                logger.error("invalid type: " + type);
                System.exit(2);
        }
        controller.run();
    }
}
