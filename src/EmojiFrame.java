import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class EmojiFrame extends JFrame
{
    private JTextField inputText;
    private JScrollPane scrollPane;
    private JPanel panel;

    private static int[] emojiCodes = {0x1F349, 0x1F603, 0x1F604, 0x1F601, 0x1F606, 0x1F605, 0x1F923, 0x1F602,
                                       0x1F642, 0x1F60A, 0x1F607, 0x1F970, 0x1F60D, 0x1F618, 0x1F972, 0x1F60B, 
                                       0x1F917, 0x1FAE3, 0x1F914, 0x1F634, 0x1F975, 0x1F973, 0x1F60E, 0x1F9D0, 
                                       0x1F622, 0x1F62D, 0x1F613, 0x1F4A9, 0x1F921, 0x1F47E, 0x2764 , 0x1F44C,
                                       0x270C , 0x1F448, 0x1F449, 0x1F44F, 0x1F64F, 0x1F440, 0x1F339, 0x1F954};
                                       //8*5 = 40 selected emojis

    public EmojiFrame(JTextField inputText)
    {
        this.inputText = inputText;

        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setTitle("EmojiBoard");
        this.setSize(800, 820);
        this.setResizable(false);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.scrollPane = new JScrollPane();
        this.scrollPane.setSize(788, 780);
        this.panel = new JPanel();
        initializeContent();
        this.scrollPane.setViewportView(panel);
        this.add(scrollPane);
    }

    public void initializeContent()
    {
        panel.setLayout(new GridLayout(8, 5, 6, 6));
        for(int emojiCode: emojiCodes)
        {
            panel.add(new EmojiButton(new String(Character.toChars(emojiCode)), this));
        }
    }

    class EmojiButton extends JButton implements ActionListener
    {
        private EmojiFrame emojiFrame;
        public EmojiButton(String text, EmojiFrame emojiFrame)
        {
            super(text);
            this.emojiFrame = emojiFrame;
            this.setPreferredSize(new Dimension(80, 50));
            this.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent)
        {
            inputText.setText(inputText.getText() + this.getText());
            emojiFrame.setVisible(false);
        }   
    }
}
