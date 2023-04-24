import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

public class ConnectPanel extends JPanel implements ActionListener
{
    private Client client;
    private CLientMainFrame mainFrame;

    private JLabel servernameLabel;
    private JLabel portLabel;
    private JTextField servernameText;
    private JTextField portText;
    private JButton connectButton;

    public ConnectPanel(Client client, CLientMainFrame mainFrame)
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
        servernameLabel = new JLabel("servername: ");
        servernameLabel.setBounds(360+2, 240+2, 120-4, 30-4);
        this.add(servernameLabel);

        portLabel = new JLabel("port: ");
        portLabel.setBounds(360+2, 270+2, 120-4, 30-4);
        this.add(portLabel);

        servernameText = new JTextField();
        servernameText.setBounds(480+2, 240+2, 240-4, 30-4);
        this.add(servernameText);

        portText = new JTextField();
        portText.setBounds(480+2, 270+2, 240-4, 30-4);
        this.add(portText);

        connectButton = new JButton("connect");
        connectButton.setBounds(450+2, 330+2, 180-4, 30-4);
        connectButton.addActionListener(this);
        connectButton.setFocusable(false);
        this.add(connectButton);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        if(actionEvent.getSource() == connectButton)
        {
            String servername = servernameText.getText();
            servernameText.setText("");
            int port = Integer.parseInt(portText.getText());
            portText.setText("");
            try
            {
                client.connect(servername, port);
                mainFrame.updateContentPanel(new LoginPanel(client, mainFrame));
            }
            catch(IllegalArgumentException e)
            {
                JOptionPane.showMessageDialog(this, "servername or port illegal", "Illegal Argument", JOptionPane.PLAIN_MESSAGE);
            }
            catch(ServerException e)
            {
                JOptionPane.showMessageDialog(this, e.getErrorMessage(), "Sever Exception", JOptionPane.PLAIN_MESSAGE);
            }
            catch(IOException e)
            {
                JOptionPane.showMessageDialog(this, "fail to connect to " + servername + ":" + port, "Connection Failed", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }
}
