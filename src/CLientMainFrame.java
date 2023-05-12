import javax.swing.JFrame;
import javax.swing.JPanel;

public class CLientMainFrame extends JFrame
{
    private JPanel contentPanel;

    /**
     * initialize the frame and set contentPanel into connectPanel
     * @param client
     */
    public CLientMainFrame(Client client)
    {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("TeleBoard");
        this.setSize(1080, 720);
        this.setResizable(false);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.contentPanel = new ConnectPanel(client, this);
        this.add(contentPanel);
        this.setVisible(true);
    }

    public void updateContentPanel(JPanel newContentPanel)
    {
        this.remove(contentPanel);
        this.add(contentPanel = newContentPanel);
        this.repaint();
        this.validate();
    }

    public void close()
    {
        this.dispose();
    }
}
