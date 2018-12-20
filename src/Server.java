import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Server {

    private static final int RAND_RANGE_TIME = 3000;
    private static final int RAND_RANGE_BTN = 15;
    private static final int RAND_RANGE_FOR = 3;

    private boolean started = false;
    private Random myrand = new Random();

    long buttonStartTime, buttonStopTime;
    private boolean running = false;

    private int port;
    private ArrayList<Connection> connections = new ArrayList<>();
    private ArrayList<ActionListener> listeners = new ArrayList<>();

    private ActionListener broadcastListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {

           /* for (Connection c : connections) {
                c.sendMessage(((Connection) e.getSource()).getUsername() + ": " + e.getActionCommand());
            }*/

            if(e.getID() == 2 ){
                switch (e.getActionCommand()){
                    case "start":
                        delayTimer.start();
                        break;

                    case "done":
                        buttonStopTime = System.currentTimeMillis();
                        long elapsedTime = (buttonStopTime - buttonStartTime);
                        Connection c = (Connection)e.getSource();
                        c.sendMessage(Long.toString(elapsedTime));
                        break;
                }
            }

            if (e.getID() == 0) {
                for (Connection c : connections) {
                    c.sendMessage(((Connection) e.getSource()).getUsername() + ": " + e.getActionCommand());
                }

            }

        }
    };


    public Timer delayTimer = new Timer(myrand.nextInt(RAND_RANGE_TIME) + 3000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            //int[] selectedButtons = new int[2];
            ArrayList<Integer> selectedButtons = new ArrayList<Integer>();
            delayTimer.stop();
            delayTimer.setDelay(myrand.nextInt(RAND_RANGE_TIME) + 3000);

            for (int i = 0; i <= myrand.nextInt(RAND_RANGE_FOR); i++) {

                selectedButtons.add(myrand.nextInt(RAND_RANGE_BTN));

                //selectedButtons[0] = myrand.nextInt(RAND_RANGE_BTN);

            }


            for(Iterator<Connection> c = connections.iterator(); c.hasNext(); ) {
                Connection connection = c.next();

                for(Iterator<Integer> i = selectedButtons.iterator(); i.hasNext();){
                    Integer selectedButton = i.next();
                    connection.sendMessage(String.valueOf(selectedButton));
                }
                System.out.println(connection);
            }

            buttonStartTime = System.currentTimeMillis();
        }
    });


    public Server(int port) {
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
                try (ServerSocket server = new ServerSocket(Server.this.port)) {

                    // loop for managing our incoming connections
                    while (running) {
                        // server.accept() gives us a socket object
                        // this socket object is used to "talk" with the client
                        // wrap ChatConnection object around this incoming socket object
                        Connection conn = new Connection(server.accept());

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
                        Connection c = connections.get(0);

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

    public ArrayList<Connection> getConnections() {
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
        Server server = new Server(1234);

        ActionListener l = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (actionEvent.getActionCommand().equalsIgnoreCase("EXIT")) {
                    ArrayList<Connection> list = server.getConnections();

                    for (Connection chatConnection : list) {
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
