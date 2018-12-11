import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ChatClient {

    private String host;
    private int port;
    private ChatConnection connection;
    private ArrayList<ActionListener> listeners = new ArrayList<>();


    public ChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {

        try {
            connection = new ChatConnection(new Socket(host, port));

            for(ActionListener listener : listeners) {
                connection.addMessageListener(listener);
            }

            connection.start();

        } catch (IOException e) {
            connection.stop();
            connection.close();
        }
    }

    public boolean isConnected() {
        return connection.isConnected();
    }

    public void addActionListener(ActionListener listener) {
        listeners.add(listener);
    }

    public void stop() {

        if (connection != null) {
            connection.stop();
            connection.close();
        }
    }

    public void sendMessage(String message) {
        connection.sendMessage(message);
    }


    public static void main(String[] args) {
        // create a new instance of out ChatClient Object
        // We need to define the host and port our server is running
        ChatClient client = new ChatClient("localhost", 1234);

        client.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(e.getActionCommand());
            }
        });

        client.start();

        if (client.isConnected()) {
            Scanner input = new Scanner(System.in);

            while (client.isConnected()) {
                client.sendMessage(input.nextLine());

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
