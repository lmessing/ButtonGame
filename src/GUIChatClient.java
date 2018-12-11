import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIChatClient extends JFrame {
    Client chatClient;
    private JTextArea messageTextArea = new JTextArea();
    private JTextField sendArea = new JTextField();
    private JButton sendButton = new JButton("Send");
    JPanel sendPanel = new JPanel(new BorderLayout());

    public GUIChatClient() throws HeadlessException {
        setLayout(new BorderLayout());
        setTitle("GUI Chat");
        setSize(300, 300);

        add(new JScrollPane(messageTextArea), BorderLayout.CENTER);
        add(sendPanel, BorderLayout.SOUTH);
        sendPanel.add(sendArea, BorderLayout.CENTER);
        sendPanel.add(sendButton, BorderLayout.EAST);

        messageTextArea.setEditable(false);

        chatClient = new Client("localhost", 1234);


        ActionListener sendListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                chatClient.sendMessage(getMessage());
                sendArea.setText("");
            }
        };

        sendArea.addActionListener(sendListener);
        sendButton.addActionListener(sendListener);


        ActionListener receiveListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                messageTextArea.append(actionEvent.getActionCommand() + "\n");
            }
        };

        chatClient.addActionListener(receiveListener);


        chatClient.start();
        setVisible(true);
    }

    private String getMessage() {
        return sendArea.getText();
    }

    public static void main(String[] args) {
        GUIChatClient guiChatClient = new GUIChatClient();
    }
}
