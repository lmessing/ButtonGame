import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIChatClient extends JFrame {


    private JTextArea messageTextArea = new JTextArea();
    private JLabel timeLabel = new JLabel();
    private Timer delayTimer;

    JButton[] buttons = new JButton[16];
    JPanel buttonsPanel = new JPanel();
    JButton btn_start = new JButton("Start");

    private boolean started = false;
    private String Message;

    Client chatClient;

    public GUIChatClient() throws HeadlessException {
        setLayout(new BorderLayout());
        setTitle("GUI Chat");
        setSize(300, 300);

        this.add(buttonsPanel, BorderLayout.CENTER);

        JPanel SouthPanel = new JPanel(new BorderLayout());
        SouthPanel.add(btn_start,BorderLayout.EAST);
        SouthPanel.add(timeLabel,BorderLayout.WEST);
        this.add(SouthPanel,BorderLayout.SOUTH);

        buttonsPanel.setLayout(new GridLayout(4, 4));

        ActionListener ButtonListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JButton button = (JButton) e.getSource();
                if (button.isEnabled()) {
                    button.setEnabled(false);
                    button.setBackground(Color.GRAY);
                }
                if (checkButtons()) {
                    btn_start.setEnabled(true);
                    started = false;
                    chatClient.sendMessage("done");
                }

            }
        };

        int i = 0;
        for (JButton button : buttons) {
            buttons[i] = new JButton();
            buttonsPanel.add(buttons[i]);
            buttons[i].setBackground(Color.GRAY);
            buttons[i].setEnabled(false);
            buttons[i].addActionListener(ButtonListener);
            i++;
        }
        btn_start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!started) {
                    started = true;
                    chatClient.sendMessage("start");
                    btn_start.setEnabled(false);
                }
            }
        });


        chatClient = new Client("localhost", 1234);

        ActionListener receiveListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Message = actionEvent.getActionCommand();
                System.out.println("Neue Nachricht empfangen: " + actionEvent.getActionCommand());
                if (Integer.parseInt(actionEvent.getActionCommand()) > 16) {
                    timeLabel.setText(actionEvent.getActionCommand()+"ms");
                } else {
                    if (!buttons[Integer.parseInt(actionEvent.getActionCommand())].isEnabled()) {
                        buttons[Integer.parseInt(actionEvent.getActionCommand())].setEnabled(true);
                        buttons[Integer.parseInt(actionEvent.getActionCommand())].setBackground(Color.GREEN);
                        btn_start.setEnabled(false);
                    }

                }
            }
        };

        chatClient.addActionListener(receiveListener);
        chatClient.start();
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
