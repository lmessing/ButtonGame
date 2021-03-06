import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Connection {
    // Connection object (Server -> Client or Client -> Server)
    private Socket socket;

    // receive data
    private DataInputStream receive;
    // send data
    private DataOutputStream send;

    private String username = "";

    // list containing all listeners
    private ArrayList<ActionListener> listeners = new ArrayList<>();

    private boolean running = false;

    public Connection(Socket connection) {
        socket = connection;

        try {
            // initialize the streams
            receive = new DataInputStream(socket.getInputStream());
            send = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            Connection.this.stop();
            Connection.this.close();
        }
    }

    /**
     * Start the connection
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
                // loop for managing our incoming connections
                while (running) {
                    try {
                        // Receive messages from the other side
                        String message = receive.readUTF();

                         if (username.length() <= 0) {
                            username = message;
                        } else {
                             if(message.equals("start") || message.equals("done")) {
                                 sendServerCommand(message);
                             }

                             else if (between(Integer.parseInt(message),0,15) == true) {
                                 sendSelected(message);
                             }

                             else {
                                 sendTime(message);
                             }
                        }

                        // notifyListeners(message);

                    } catch (IOException e) {
                        Connection.this.stop();
                        Connection.this.close();
                    }
                }
                // at this point the running variable is false
                // stop() was called
            }
        }.start();
    }

    /**
     * Checks if the connection is established
     *
     * @return isConnected
     */
    public boolean isConnected() {
        return running;
    }

    /**
     * stop the connection
     */
    public void stop() {
        running = false;
    }

    /**
     * close all in- and outputstreams
     * close the socket connection
     */
    public void close() {
        try {
            send.close();
            receive.close();

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * send message to the other side
     *
     * @param message the message to send
     */
    public void sendMessage(String message) {
        try {
            send.writeUTF(message);
            send.flush();
        } catch (IOException e) {
            Connection.this.stop();
            Connection.this.close();
        }
    }

    /**
     * send selected buttons to other client
     *
     * @param selectedButton
     */
    public void sendSelected(String selectedButton) {
        for (ActionListener l : listeners) {
            l.actionPerformed(new ActionEvent(this, 1, selectedButton));
        }
    }

    public void sendServerCommand(String command) {
        for (ActionListener l : listeners) {
            l.actionPerformed(new ActionEvent(this, 2, command));
        }
    }

    public void sendTime(String time) {
        for (ActionListener l : listeners) {
            l.actionPerformed(new ActionEvent(this, 0, time));
        }
    }

    public void sendIP(String ip){
        for (ActionListener l : listeners) {
            l.actionPerformed(new ActionEvent(this, 3, ip));
        }
    }

    /**
     * notify all listeners: There is a new message
     *
     * @param message
     */
    private void notifyListeners(String message) {
        for (ActionListener l : listeners) {
            l.actionPerformed(new ActionEvent(this, 0, message));
        }
    }

    /**
     * Add a new Listener to our List
     *
     * @param l
     */
    public void addMessageListener(ActionListener l) {
        listeners.add(l);
    }

    public String getUsername() {
        return username;
    }

    public static boolean between(int i, int minValueInclusive, int maxValueInclusive) {
        if (i >= minValueInclusive && i <= maxValueInclusive)
            return true;
        else
            return false;
    }
}
