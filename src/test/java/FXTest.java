import Server.*;
import org.junit.jupiter.api.Test;


/**
 * Eine Test Klasse die,die Oberflöche startet ohne zuerst den Server und dann den Client zu starten.
 */

public class FXTest {
    @Test
    public void FXTest(){

        Thread thread = new Thread() {
            public void run() {

                Server_Socket server_socket = new Server_Socket(8000);
                server_socket.run();

            }
        };
        thread.start();

        Client.Main.main(null);

    }
}
