import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ChatServer {
    private int port;
    private ArrayList<ChatConnection> connections = new ArrayList<>();
    private ArrayList<ActionListener> listeners = new ArrayList<>();

    private ActionListener broadcastListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            for (ChatConnection c : connections) {
                c.sendMessage(((ChatConnection) e.getSource()).getUsername() + ": " + e.getActionCommand());
            }
        }
    };

    private boolean running = false;

    public ChatServer(int port) {
        this.port = port;
    }

    /**
     * Start the Server
     */
    public void start() {
        // boolean variable
        // --> mark the server as running
        // variable used in the while loop in our thread
        running = true;

        // anonymous implementation of the thread
        // we need to override our run method
        new Thread() {
            @Override
            public void run() {
                // try with resources
                // Class MUST implement java.io.
                try (ServerSocket server = new ServerSocket(ChatServer.this.port)) {

                    // loop for managing our incoming connections
                    while (running) {
                        // server.accept() gives us a socket object
                        // this socket object is used to "talk" with the client
                        // wrap ChatConnection object around this incoming socket object
                        ChatConnection conn = new ChatConnection(server.accept());

                        // Messagelisteners listen to incoming messages
                        // --> messages from the client to the server
                        conn.addMessageListener(broadcastListener);

                        for (ActionListener al : listeners) {
                            conn.addMessageListener(al);
                        }

                        // add the incoming client to the list of our
                        // recent connections
                        connections.add(conn);

                        // add another Messagelistener
                        // this messagelistener just receives the incoming message
                        // and writes it to the console.
                        conn.addMessageListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                System.out.println("Neue Nachricht empfangen: " + e.getActionCommand());
                            }
                        });

                        // start the server thread that listens to incoming messages
                        // if there is a new message --> notify all listeners
                        conn.start();
                    }

                    // at this point the running variable is false
                    // stop() was called

                    /*
                     * Close all ChatConnections
                     * --> close them AND remove them from our list
                     * do while our list is not empty (there are still elements in our list remaining)
                     */
                    while (connections.size() > 0) {
                        // remove all existing connections from the list
                        // take the first element from the list
                        ChatConnection c = connections.get(0);

                        // and remove it
                        connections.remove(c);

                        // close the thread that listens to incoming messages
                        c.stop();

                        // close the connection to the client
                        c.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public ArrayList<ChatConnection> getConnections() {
        return connections;
    }

    /**
     * stop the server
     */
    public void stop() {
        running = false;
    }

    public void addMessageListener(ActionListener al) {
        listeners.add(al);
    }


    /**
     * Start a new Chat - Server
     *
     * @param args
     */
    public static void main(String[] args) {
        // create a new instance of out ChatServer Object
        // We need to define the port out server is running
        ChatServer server = new ChatServer(1234);

        ActionListener l = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (actionEvent.getActionCommand().equalsIgnoreCase("EXIT")) {
                    ArrayList<ChatConnection> list = server.getConnections();

                    for (ChatConnection chatConnection : list) {
                        if (actionEvent.getSource().equals(chatConnection)) {
                            list.remove(chatConnection);

                            chatConnection.stop();
                            chatConnection.close();

                            break;
                        }
                    }
                }
            }
        };

        server.addMessageListener(l);

        // start the server thread
        // listen to incoming connections
        server.start();
    }
}
