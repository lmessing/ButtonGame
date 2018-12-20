import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIChatClient extends JFrame {


    private JTextArea messageTextArea = new JTextArea(1,15);
    private JLabel timeLabel = new JLabel();
    JButton[] buttons = new JButton[16];
    JPanel buttonPanel = new JPanel();
    JButton buttonStart = new JButton("Start");
    JPanel southPanel = new JPanel(new BorderLayout());
    JPanel timePanel = new JPanel(new BorderLayout());
    JPanel mainPanel = new JPanel(new GridLayout(1,2));
    JPanel userPanel = new JPanel(new BorderLayout());
    private JTextField sendArea = new JTextField();
    private JButton buttonSend = new JButton("Send");
    private boolean started = false;
    private String message;
    Client client;
    //private Timer delayTimer;

    public GUIChatClient() throws HeadlessException {
        setLayout(new BorderLayout());
        setTitle("GUI Chat");
        setSize(900, 600);

        southPanel.add(buttonStart,BorderLayout.EAST);
        southPanel.add(messageTextArea, BorderLayout.CENTER);
        southPanel.add(timeLabel,BorderLayout.WEST);

        mainPanel.add(buttonPanel);
        mainPanel.add(timePanel);

        userPanel.add(sendArea, BorderLayout.CENTER);
        userPanel.add(buttonSend, BorderLayout.EAST);

        timePanel.add(new JScrollPane(messageTextArea), BorderLayout.CENTER);
        timePanel.add(userPanel, BorderLayout.SOUTH);

        this.add(mainPanel, BorderLayout.CENTER);
        this.add(southPanel,BorderLayout.SOUTH);
        this.add(timePanel, BorderLayout.EAST);

        messageTextArea.setEditable(false);

        buttonPanel.setLayout(new GridLayout(4, 4));



        ActionListener ButtonListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JButton button = (JButton) e.getSource();
                if (button.isEnabled()) {
                    button.setEnabled(false);
                    button.setBackground(Color.GRAY);
                }
                if (checkButtons()) {
                    buttonStart.setEnabled(true);
                    started = false;
                    client.sendMessage("done");
                }

            }
        };

        int i = 0;
        for (JButton button : buttons) {
            buttons[i] = new JButton();
            buttonPanel.add(buttons[i]);
            buttons[i].setBackground(Color.GRAY);
            buttons[i].setEnabled(false);
            buttons[i].addActionListener(ButtonListener);
            i++;
        }

        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!started) {
                    started = true;
                    client.sendMessage("start");
                    buttonStart.setEnabled(false);
                }
            }
        });


        client = new Client("localhost", 1234);

        ActionListener receiveListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                message = actionEvent.getActionCommand();

                if(actionEvent.getID() == 1) {
                    int selectedButton = Integer.parseInt(message);

                    if (!buttons[selectedButton].isEnabled()) {
                        buttons[selectedButton].setEnabled(true);
                        buttons[selectedButton].setBackground(Color.GREEN);
                        buttonStart.setEnabled(false);
                    }
                }

                if(actionEvent.getID() == 0) {
                    System.out.println("Neue Nachricht empfangen: " +message);
                    timeLabel.setText("Deine Zeit: " +message + "ms\n");
                    messageTextArea.append(message + "ms\n");
                }
            }
        };

        ActionListener sendListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                client.sendMessage(getMessage());
                sendArea.setText("");
            }
        };

        sendArea.addActionListener(sendListener);
        buttonSend.addActionListener(sendListener);

        client.addActionListener(receiveListener);
        client.start();
        setVisible(true);
    }

    public boolean checkButtons() {
        boolean status = false;

        int j = 0;
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].isEnabled()) {
                j++;
            }
        }
        if (j == 0) {
            status = true;
        }
        return (status);
    }

    private String getMessage() {
        return sendArea.getText();
    }

    public static void main(String[] args) {
        GUIChatClient guiChatClient = new GUIChatClient();
    }
}
