package Server;

import Client.Main;

public class TestMainEclipse {
    public static void main(String[] args) {
        Thread thread = new Thread() {
            public void run() {

                Server.Main.main(null);
            }
        };
        thread.start();

        Main.main(null);
    }
}
