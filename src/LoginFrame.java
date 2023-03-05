import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class LoginFrame {
    private JTextField usernameText;
    private JPasswordField passwordPassword;
    private JButton loginButton;
    private JButton registerButton;
    private JPanel rootPanel;
    private JLabel notificationLabel;

    public LoginFrame() {
        Client client = new Client();
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameText.getText();
                String password = Arrays.toString(passwordPassword.getPassword());
                usernameText.setText("");
                passwordPassword.setText("");
                int resultCode = client.login(username, password);
            }
        });
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameText.getText();
                String password = Arrays.toString(passwordPassword.getPassword());
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("LoginFrame");
        frame.setContentPane(new LoginFrame().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
