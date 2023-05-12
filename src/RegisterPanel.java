import java.awt.event.*;
import javax.swing.*;
import java.io.IOException;

public class RegisterPanel extends JPanel implements ActionListener
{
    private Client client;
    private CLientMainFrame mainFrame;

    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField usernameText;
    private JPasswordField passwordPassword;
    private JLabel ageLabel;
    private JTextField ageText;
    private JLabel genderLabel;
    private JTextField genderText;
    private JLabel addressLabel;
    private JTextField addressText;
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
        usernameLabel.setBounds(360+2, 210+2, 120-4, 30-4);
        this.add(usernameLabel);

        passwordLabel = new JLabel("password: ");
        passwordLabel.setBounds(360+2, 240+2, 120-4, 30-4);
        this.add(passwordLabel);

        usernameText = new JTextField();
        usernameText.setBounds(480+2, 210+2, 240-4, 30-4);
        this.add(usernameText);

        passwordPassword = new JPasswordField();
        passwordPassword.setBounds(480+2, 240+2, 240-4, 30-4);
        this.add(passwordPassword);

        ageLabel = new JLabel("age: ");
        ageLabel.setBounds(360+2, 270+2, 120-4, 30-4);
        this.add(ageLabel);

        ageText = new JTextField();
        ageText.setBounds(480+2, 270+2, 240-4, 30-4);
        this.add(ageText);

        genderLabel = new JLabel("gender: ");
        genderLabel.setBounds(360+2, 300+2, 120-4, 30-4);
        this.add(genderLabel);

        genderText = new JTextField();
        genderText.setBounds(480+2, 300+2, 240-4, 30-4);
        this.add(genderText);

        addressLabel = new JLabel("address: ");
        addressLabel.setBounds(360+2, 330+2, 120-4, 30-4);
        this.add(addressLabel);

        addressText = new JTextField();
        addressText.setBounds(480+2, 330+2, 240-4, 30-4);
        this.add(addressText);

        gobackButton = new JButton("go back");
        gobackButton.setBounds(360+2, 360+2, 180-4, 30-4);
        gobackButton.addActionListener(this);
        gobackButton.setFocusable(false);
        this.add(gobackButton);

        registerButton = new JButton("register");
        registerButton.setBounds(540+2, 360+2, 180-4, 30-4);
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
            String password = String.valueOf(passwordPassword.getPassword());
            int age;
            try
            {
                age = Integer.parseInt(ageText.getText());
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(this, "Age must be a number", "Input Error", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            String gender = genderText.getText();
            for(char ch: gender.toCharArray())
            {
                if(ch >= 'a' && ch <= 'z') continue;
                if(ch >= 'A' && ch <= 'Z') continue;
                JOptionPane.showMessageDialog(this, "Gender can only include a-z and A-Z", "Input Error", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            String address = addressText.getText();
            for(char ch: gender.toCharArray())
            {
                if(ch >= 'a' && ch <= 'z') continue;
                if(ch >= 'A' && ch <= 'Z') continue;
                if(ch == ' ') continue;
                JOptionPane.showMessageDialog(this, "Address can only include a-z, A-Z and space", "Input Error", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            usernameText.setText("");
            passwordPassword.setText("");
            ageText.setText("");
            genderText.setText("");
            addressText.setText("");
            try
            {
                client.register(username, password, age, gender, address);
                mainFrame.updateContentPanel(new LoginPanel(client, mainFrame));
            }
            catch(LoginException e)
            {
                JOptionPane.showMessageDialog(this, e.getErrorMessage(), "Login Failed", JOptionPane.PLAIN_MESSAGE);
            }
            catch(ServerException e)
            {
                JOptionPane.showMessageDialog(this, e.getErrorMessage(), "Connection Failed", JOptionPane.ERROR_MESSAGE);
                mainFrame.close();
            }
            catch(IOException e)
            {
                JOptionPane.showMessageDialog(this, "It seems we have lost connection with the server.", "Connection Failed", JOptionPane.ERROR_MESSAGE);
                mainFrame.close();
            }
        }
    }
}
