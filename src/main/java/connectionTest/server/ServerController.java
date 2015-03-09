package connectionTest.server;

public class ServerController implements Runnable {

    @Override
    public void run() {
        new Server().start();
    }
}
