package connectionTest.client;

import java.util.Scanner;

import static connectionTest.client.Client.logger;

public class ClientController implements Runnable {

    private final String host;
    private final Scanner sc = new Scanner(System.in);

    private Client client;

    public ClientController(String host) {
        this.host = host;
    }

    @Override
    public void run() {
        client = new Client(host);
        String clientId = readClientId();
        client.connect(clientId);
        consoleLoop();
        client.terminate();
    }

    private void consoleLoop() {
        for (;;) {
            printMenu();
            String input = sc.nextLine();
            int i;
            try {
                i = Integer.valueOf(input);
            }
            catch (NumberFormatException e) {
                logger.error("Invalid number: " + input);
                continue;
            }
            switch (i) {
                case 1:
                    sendMessage();
                    break;
                case 2:
                    disconnect();
                    break;
                case 3:
                    reconnect();
                    break;
                case 4:
                    logger.info("Exiting...");
                    return;
            }
        }
    }

    private String readClientId() {
        System.out.println("Id of the client:");
        return sc.nextLine();
    }

    private void sendMessage() {
        ensureConnected(() -> {
            System.out.println("Message to send:");
            String msg = sc.nextLine();
            logger.info("Ok so send this: {}", msg);
            client.send(msg);
        });
    }

    private void disconnect() {
        ensureConnected(() ->
            client.disconnect()
        );
    }

    private void reconnect() {
        ensureNotConnected(() ->
            client.reconnect()
        );
    }

    private void ensureConnected(Runnable runnable) {
        if (client.isConnected()) {
           runnable.run();
        }
        else {
            Client.logger.error("Client not connected!");
        }
    }

    private void ensureNotConnected(Runnable runnable) {
        if (client.isConnected()) {
            Client.logger.error("Client already connected!");
        }
        else {
            runnable.run();
        }
    }

    private void printMenu() {
        logger.info("{}\n{}", client,
            "===============\n" +
            "1. send message\n" +
            "2. disconnect\n" +
            "3. reconnect\n" +
            "4. exit\n" +
            "---------------"
        );
    }
}
