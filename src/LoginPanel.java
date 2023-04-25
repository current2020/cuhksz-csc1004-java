import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

public class LoginPanel extends JPanel implements ActionListener
{
    private Client client;
    private CLientMainFrame mainFrame;

    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField usernameText;
    private JPasswordField passwordPassword;
    private JButton loginButton;
    private JButton goregisterButton;

    public LoginPanel(Client client, CLientMainFrame mainFrame)
    {
        this.client = client;
        this.mainFrame = mainFrame;
        this.setLayout(null);
        this.setSize(1080, 720);
        this.initializeContent();
        this.setVisible(true);
    }

    private void initializeContent()
    {
        usernameLabel = new JLabel("username: ");
        usernameLabel.setBounds(360+2, 240+2, 120-4, 30-4);
        this.add(usernameLabel);

        passwordLabel = new JLabel("password: ");
        passwordLabel.setBounds(360+2, 270+2, 120-4, 30-4);
        this.add(passwordLabel);

        usernameText = new JTextField();
        usernameText.setBounds(480+2, 240+2, 240-4, 30-4);
        this.add(usernameText);

        passwordPassword = new JPasswordField();
        passwordPassword.setBounds(480+2, 270+2, 240-4, 30-4);
        this.add(passwordPassword);

        loginButton = new JButton("login");
        loginButton.setBounds(360+2, 330+2, 180-4, 30-4);
        loginButton.addActionListener(this);
        loginButton.setFocusable(false);
        this.add(loginButton);

        goregisterButton = new JButton("go register");
        goregisterButton.setBounds(540+2, 330+2, 180-4, 30-4);
        goregisterButton.addActionListener(this);
        goregisterButton.setFocusable(false);
        this.add(goregisterButton);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        if(actionEvent.getSource() == loginButton)
        {
            String username = usernameText.getText();
            usernameText.setText("");
            String password = String.valueOf(passwordPassword.getPassword());
            passwordPassword.setText("");
            try
            {
                ChatPanel chatPanel = new ChatPanel(client, mainFrame, username);
                client.login(username, password, chatPanel);
                mainFrame.updateContentPanel(chatPanel);
            }
            catch(LoginException e)
            {
                JOptionPane.showMessageDialog(this, e.getErrorMessage(), "Login Failed", JOptionPane.PLAIN_MESSAGE);
            }
            catch(ServerException e)
            {
                JOptionPane.showMessageDialog(this, e.getErrorMessage(), "Connection Failed", JOptionPane.ERROR_MESSAGE);
                mainFrame.quit();
            }
            catch(IOException e)
            {
                JOptionPane.showMessageDialog(this, "It seems we have lost connection with the server.", "Connection Failed", JOptionPane.ERROR_MESSAGE);
                mainFrame.quit();
            }
        }
        else if(actionEvent.getSource() == goregisterButton)
        {
            mainFrame.updateContentPanel(new RegisterPanel(client, mainFrame));
        }
    }
}
