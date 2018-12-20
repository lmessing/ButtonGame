import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.util.Hashtable;

public class GUIChatClient extends JFrame {


    private JTextArea messageTextArea = new JTextArea(1,15);
    private JLabel timeLabel = new JLabel();
    JButton[] buttons = new JButton[16];
    JPanel buttonPanel = new JPanel();
    JButton buttonStart = new JButton("Start");
    JPanel southPanel = new JPanel(new BorderLayout());
    JPanel timePanel = new JPanel(new BorderLayout());
    JPanel mainPanel = new JPanel(new GridLayout(1,2));
    JPanel userPanel = new JPanel(new FlowLayout());
    private boolean started = false;
    private String message;
    Client client;

    public GUIChatClient() throws HeadlessException {
        setLayout(new BorderLayout());
        setTitle("GUI Chat");
        setSize(600, 400);


        southPanel.add(buttonStart,BorderLayout.EAST);
      //  southPanel.add(messageTextArea, BorderLayout.CENTER);
        southPanel.add(timeLabel,BorderLayout.WEST);

        mainPanel.add(buttonPanel);
        mainPanel.add(timePanel);

        timePanel.add(new JScrollPane(messageTextArea), BorderLayout.CENTER);

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



    public static void main(String[] args) {
        GUIChatClient guiChatClient = new GUIChatClient();
    }
}
