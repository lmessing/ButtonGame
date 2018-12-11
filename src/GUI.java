import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class GUI extends JFrame {

    private JButton topButton = new JButton("Start");
    private JButton bottomButton = new JButton("Stop");
    private static final int RAND_RANGE_TIME = 3000;
    private static final int RAND_RANGE_BTN = 15;
    private static final int RAND_RANGE_FOR = 3;
    private int i = 0;
    Timer timer;
    JButton[] buttons = new JButton[RAND_RANGE_BTN + 1];
    Random myrand = new Random();
    private JTextArea messageTextArea = new JTextArea();
    private JLabel timeLabel = new JLabel("tswtet");
    Client GameClient;

    public GUI(String title) {
        super(title);
        this.setSize(400, 400);

      //  Random random = new Random();
      //  int value = random.nextInt(2) + 5;

        GameClient = new Client("localhost", 1234);

        bottomButton.setEnabled(false);
        this.setLayout(new BorderLayout());
        this.add(topButton, BorderLayout.NORTH);
        this.add(bottomButton,BorderLayout.SOUTH);
        this.add(timeLabel, BorderLayout.EAST);

        JPanel buttonsPanel = new JPanel();
        JPanel labelPanel = new JPanel();

        addButtons(buttonsPanel);
        this.add(buttonsPanel, BorderLayout.CENTER);

        topButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                new Thread() {
                    public void run() {

                        topButton.setEnabled(false);
                        bottomButton.setEnabled(true);
                        timer = new Timer(myrand.nextInt(RAND_RANGE_TIME + 3000), TimerListener);
                        timer.start();

                        bottomButton.setEnabled(true);
                        GameClient.start();

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }

                    }
                }.start();
            }
        });

        this.add(buttonsPanel, BorderLayout.CENTER);

        bottomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                bottomButton.setEnabled(false);
                topButton.setEnabled(true);
                timer.stop();
            }
        });
        ActionListener receiveListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                messageTextArea.append(actionEvent.getActionCommand() + "\n");
            }
        };

        GameClient.addActionListener(receiveListener);



        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void addButtons(JPanel buttonsPanel) {
        buttonsPanel.setLayout(new GridLayout(4, 4));

        for (JButton button : buttons) {
            buttons[i] = new JButton();
            buttonsPanel.add(buttons[i]);
            buttons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton button = (JButton) e.getSource();
                    if (button.isEnabled()) {
                        button.setEnabled(false);
                        button.setBackground(Color.GRAY);
                    }
                    if (checkButtons()) {
                        timer = new Timer(myrand.nextInt(RAND_RANGE_TIME + 3000), TimerListener);
                        timer.restart();
                    }
                }
            });
            buttons[i].setBackground(Color.GRAY);
            buttons[i].setEnabled(false);
            i++;
        }


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

    public ActionListener TimerListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
            timer.stop();
            int rand_for = myrand.nextInt(RAND_RANGE_FOR);

            for (int i = 0; i <= rand_for; i++) {

                int id = myrand.nextInt(RAND_RANGE_BTN);
                boolean hasWorked = false;

                while (!hasWorked) {
                    if (buttons[id].isEnabled()) {
                        id = myrand.nextInt(RAND_RANGE_BTN);
                    } else {
                        buttons[id].setEnabled(true);
                        GameClient.sendMessage(String.valueOf(id));
                        buttons[id].setBackground(Color.GREEN);
                        hasWorked = true;
                    }
                }
            }
        }
    };
}
