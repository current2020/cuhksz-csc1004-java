import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

public class RegisterPanel extends JPanel implements ActionListener
{
    private Client client;
    private CLientMainFrame mainFrame;

    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField usernameText;
    private JPasswordField passwordPassword;
    private JButton gobackButton;
    private JButton registerButton;

    public RegisterPanel(Client client, CLientMainFrame mainFrame)
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

        gobackButton = new JButton("go back");
        gobackButton.setBounds(360+2, 330+2, 180-4, 30-4);
        gobackButton.addActionListener(this);
        gobackButton.setFocusable(false);
        this.add(gobackButton);

        registerButton = new JButton("register");
        registerButton.setBounds(540+2, 330+2, 180-4, 30-4);
        registerButton.addActionListener(this);
        registerButton.setFocusable(false);
        this.add(registerButton);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        if(actionEvent.getSource() == gobackButton)
        {
            mainFrame.updateContentPanel(new LoginPanel(client, mainFrame));
        }
        else if(actionEvent.getSource() == registerButton)
        {
            String username = usernameText.getText();
            usernameText.setText("");
            String password = String.valueOf(passwordPassword.getPassword());
            passwordPassword.setText("");
            try
            {
                client.register(username, password);
                mainFrame.updateContentPanel(new LoginPanel(client, mainFrame));
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
    }
}
