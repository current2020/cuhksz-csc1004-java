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

    private static String[] emojis = {"😀", "😃", "😄", "😁", "😆", "😅", "🤣", "😂", "🙂", "🙃", "🫠", "😉", "😊", "😇", "🥰", "😍", 
                                      "🤩", "😘", "😗", "🥲", "😋", "😛", "😜", "🤪", "😝", "🤑", "🤗", "🤭", "🫢", "🫣", "🤫", "🤔",
                                      "🫡", "🤐", "🤨", "😐", "😑", "😶", "😏", "😒", "🙄", "😬", "😮‍💨", "🤥", "😌", "😔", "😪", "🤤 ", 
                                      "😴", "😷", "🤒", "🤕", "🤢", "🤮", "🤧", "🥵", "🥶", "🥴", "😵", "😵‍💫", "🤯", "🥳", "😎", "🤓", 
                                      "🧐", "😕", "🫤", "😟", "🙁", "😮", "😯", "😲", "😳", "🥺", "🥹", "😦", "😧", "😨", "😰", "😥", 
                                      "😢", "😭", "😱", "😖", "😣", "😞", "😓", "😩", "😫", "🥱", "😤", "😡", "😠", "🤬", "👿", "💀", 
                                      "💩", "🤡", "👻", "👽", "👾", "🙈", "🙉", "🙊", "💘", "💝", "💖", "💔", "❤", "🧡", "💛", "💚", 
                                      "💙", "💜", "💋", "💢", "💥", "💫", "💦", "💨", "💤", "👋", "👌", "🤏", "✌", "🫰", "🤟", "🤘", 
                                      "👈", "👉", "👆", "🖕", "👇", "☝", "🫵", "👍", "👎", "✊", "👊", "🤛", "🤜", "👏", "🫶", "🤝", 
                                      "🙏", "🤳", "💪", "👀", "🐶", "🦊", "🐱", "🐴", "🐮", "🐷", "🐭", "🐹", "🐰", "🐻", "🐔", "🐍", 
                                      "🐉", "🐛", "🌹", "🥀", "🍉", "🍋", "🍎", "🍑", "🍅", "🥥", "🥔", "🌶", "🍺", "🦀", "🦐", "🎂"};
                                      //16*11 = 176 selected emojis

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
        panel.setLayout(new GridLayout(22, 8, 6, 6));
        for(String emoji: emojis)
        {
            panel.add(new EmojiButton(emoji, this));
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
