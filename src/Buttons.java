import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Buttons extends JFrame{

    private static final int RAND_RANGE_TIME = 3000;
    private static final int RAND_RANGE_BTN = 15;
    private static final int RAND_RANGE_FOR = 3;
    private int i = 0;
    Timer timer;
    JButton[] buttons = new JButton[RAND_RANGE_BTN+1];
    Random myrand = new Random();

    //MUROK jebem ti mater

    public Buttons(JPanel buttonsPanel) {
        this.setLayout(new GridLayout(4,4));

        for (JButton button : buttons) {
            buttons[i] = new JButton();
            this.add(buttons[i]);
            buttons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton button =(JButton)e.getSource();
                    if(button.isEnabled()){
                        button.setEnabled(false);
                        button.setBackground(Color.GRAY);
                    }
                    if(checkButtons()){
                        timer = new Timer(myrand.nextInt(RAND_RANGE_TIME+3000), TimerListener);
                        timer.restart();
                    }
                }
            });
            buttons[i].setBackground(Color.GRAY);
            buttons[i].setEnabled(false);
            i++;
        }

      //  setDefaultCloseOperation(EXIT_ON_CLOSE);
       // setSize(700, 500);
      //  setVisible(true);

        buttonsPanel.add(this);
        timer = new Timer(myrand.nextInt(RAND_RANGE_TIME+3000), TimerListener);
        timer.start();
    }

    public void addButtons(){
        for (JButton button : buttons) {
            buttons[i] = new JButton();
            this.add(buttons[i]);
            buttons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton button =(JButton)e.getSource();
                    if(button.isEnabled()){
                        button.setEnabled(false);
                        button.setBackground(Color.GRAY);
                    }
                    if(checkButtons()){
                        timer = new Timer(myrand.nextInt(RAND_RANGE_TIME+3000), TimerListener);
                        timer.restart();
                    }
                }
            });
            buttons[i].setBackground(Color.GRAY);
            buttons[i].setEnabled(false);
            i++;
        }

        //  setDefaultCloseOperation(EXIT_ON_CLOSE);
        // setSize(700, 500);
        //  setVisible(true);


        timer = new Timer(myrand.nextInt(RAND_RANGE_TIME+3000), TimerListener);
        timer.start();
    }

    public ActionListener TimerListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
            timer.stop();
            int rand_for = myrand.nextInt(RAND_RANGE_FOR);

            for(int i = 0; i <= rand_for;i++) {

                int id = myrand.nextInt(RAND_RANGE_BTN);
                boolean hasWorked = false;

                while(!hasWorked) {
                    if (buttons[id].isEnabled()) {
                        id = myrand.nextInt(RAND_RANGE_BTN);
                        buttons[id].setEnabled(true);
                    } else {
                        buttons[id].setEnabled(true);
                        buttons[id].setBackground(Color.GREEN);
                        hasWorked = true;
                    }
                }
            }
        }
    };

    public boolean checkButtons(){
        boolean status = false;

        int j = 0;
        for(int i = 0; i < buttons.length; i++){
            if(buttons[i].isEnabled()){
                j++;
            }
        }
        if(j == 0){
            status = true;
        }
        return(status);
    }

}
