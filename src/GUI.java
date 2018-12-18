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
    private int randTime = 0;
    Timer delayTimer;
    long buttonStartTime, buttonStopTime;
    JButton[] buttons = new JButton[RAND_RANGE_BTN + 1];
    Random myrand = new Random();
    private JTextArea messageTextArea = new JTextArea();
    private JLabel timeLabel = new JLabel();
    JPanel buttonsPanel = new JPanel();

    Client GameClient;

    public GUI(String title) {
        super(title);
        this.setSize(600, 600);
        this.setLayout(new BorderLayout());

        GameClient = new Client("localhost", 1234);

        bottomButton.setEnabled(false);

        addButtons(buttonsPanel);
        JPanel bottomPanel = new JPanel(new GridLayout(2,1));
        bottomPanel.add(bottomButton);
        bottomPanel.add(timeLabel);

        this.add(topButton, BorderLayout.NORTH);
        this.add(bottomPanel, BorderLayout.SOUTH);
        this.add(buttonsPanel, BorderLayout.CENTER);



        topButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                new Thread() {
                    public void run() {

                        topButton.setEnabled(false);
                        bottomButton.setEnabled(true);

                        // GET RAND
                        delayTimer = new Timer((RAND_RANGE_TIME + 3000), DelayTimerListener);
                        delayTimer.start();

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


        bottomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                bottomButton.setEnabled(false);
                topButton.setEnabled(true);
                timeLabel.setText("");
                delayTimer.stop();
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
            buttons[i].setBackground(Color.GRAY);
            buttons[i].setEnabled(false);

            buttons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton button = (JButton) e.getSource();
                    if (button.isEnabled()) {
                        button.setEnabled(false);
                        button.setBackground(Color.GRAY);
                    }
                    if (checkButtons()) {
                        buttonStopTime = System.currentTimeMillis();

                        long elapsedTime = (buttonStopTime - buttonStartTime);


                        timeLabel.setText(String.valueOf(elapsedTime) + " ms");
                        //GET RAND
                        randTime = myrand.nextInt(RAND_RANGE_TIME +3000);
                        delayTimer = new Timer(randTime, DelayTimerListener);
                        delayTimer.restart();
                    }
                }
            });

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

    public ActionListener DelayTimerListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
            delayTimer.stop();
            buttonStartTime = System.currentTimeMillis();

            //GET RAND
            int rand_for = myrand.nextInt(RAND_RANGE_FOR);

            for (int i = 0; i <= rand_for; i++) {

                //GET RAND
                int id = myrand.nextInt(RAND_RANGE_BTN);
                boolean hasWorked = false;

                while (!hasWorked) {
                    if (buttons[id].isEnabled()) {


                        //GET RAND
                        id = myrand.nextInt(RAND_RANGE_BTN);
                    } else {
                        buttons[id].setEnabled(true);
                        //GameClient.sendMessage(String.valueOf(id));
                        buttons[id].setBackground(Color.GREEN);
                        hasWorked = true;
                    }
                }
            }
        }
    };
}
