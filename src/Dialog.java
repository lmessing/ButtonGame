import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Dialog extends JDialog {


    JLabel nameLabel = new JLabel("IP Adress Mitspieler: ");
    JLabel portLabel = new JLabel("Port: ");

    JTextField nameField = new JTextField();
    JTextField portField = new JTextField();

    JButton okButton = new JButton("OK");
    JButton cancelButton = new JButton("Cancel");

    public Dialog() {
        setupUI();
        setUpListeners();

    }

    public void setupUI() {

        this.setTitle("Login");

        JPanel topPanel = new JPanel(new GridBagLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(4, 4, 4, 4);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        topPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        topPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        topPanel.add(portLabel, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;
        topPanel.add(portField, gbc);

        this.add(topPanel);

        this.add(buttonPanel, BorderLayout.SOUTH);

    }

    private void setUpListeners() {

        portField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    login();
                }
            }
        });

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dialog.this.setVisible(false);
            }
        });
    }

    private void login() {
        GUIChatClient guiChatClient = new GUIChatClient();
        System.out.println("You logged in!");
        Dialog.this.setVisible(false);
    }

    public static void main(String[] args) {
        Dialog dialog = new Dialog();

        dialog.setSize(400, 150);
        dialog.setVisible(true);
    }
}
