import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.HashMap;
import java.io.IOException;

public class ChatPanel extends JPanel implements ActionListener
{
    private Client client;
    private CLientMainFrame mainFrame;
    private String username;

    private JScrollPane displayScrollPane;
    private JPanel displayPanel;
    private JPanel operatioPanel;
    private JLabel selectedLabel;
    private JTextField inputText;
    private JButton emojiButton;
    private JButton cancelButton;
    private JButton operationButton; // the same button for "send" and "edit"

    private HashMap<Integer, MessageDisplay> messageDisplays;
    private int MessageDisplayCount;
    private int operationMode; // 0 for "send", 1 for "quote and send", 2 for "edit"
    private Message selectedMessage;

    private EmojiFrame emojiFrame;

    public ChatPanel(Client client, CLientMainFrame mainFrame, String username)
    {
        this.client = client;
        this.mainFrame = mainFrame;
        this.username = username;
        mainFrame.setTitle(mainFrame.getTitle() + " " + username);

        this.messageDisplays = new HashMap<Integer, MessageDisplay>();
        this.MessageDisplayCount = 0;
        this.operationMode = 0;

        this.setLayout(null);
        this.setSize(1080, 720);
        this.initializeContent();
        this.setVisible(true);
    }

    private void initializeContent()
    {
        displayScrollPane = new JScrollPane();
        displayScrollPane.setBounds(0, 0, 1067, 590);
        this.add(displayScrollPane);

        displayPanel = new JPanel();
        displayPanel.setLayout(null);
        displayPanel.setPreferredSize(new Dimension(1047, 90));
        displayScrollPane.setViewportView(displayPanel);

        operatioPanel = new JPanel();
        operatioPanel.setLayout(null);
        operatioPanel.setBounds(0, 590, 1080, 100);
        operatioPanel.setBackground(Color.LIGHT_GRAY);
        this.add(operatioPanel);
        
        selectedLabel = new JLabel("");
        selectedLabel.setBounds(0+2, 0+2, 1070-4, 30-4);
        operatioPanel.add(selectedLabel);
        
        inputText = new JTextField();
        inputText.setBounds(0+2, 30+2, 1070-4, 30-4);
        operatioPanel.add(inputText);
        
        emojiButton = new JButton("emoji");
        emojiButton.setBounds(615+2, 60+2, 150-4, 30-4);
        emojiButton.addActionListener(this);
        emojiButton.setFocusable(false);
        operatioPanel.add(emojiButton);
        
        cancelButton = new JButton("cancel");
        cancelButton.setBounds(765+2, 60+2, 150-4, 30-4);
        cancelButton.addActionListener(this);
        cancelButton.setFocusable(false);
        cancelButton.setEnabled(false);
        operatioPanel.add(cancelButton);
        
        operationButton = new JButton("send");
        operationButton.setBounds(915+2, 60+2, 150-4, 30-4);
        operationButton.addActionListener(this);
        operationButton.setFocusable(false);
        operatioPanel.add(operationButton);
        
        emojiFrame = new EmojiFrame(inputText);
        emojiFrame.setVisible(false);
    }
    
    private void close()
    {
        mainFrame.close();
        emojiFrame.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        if(actionEvent.getSource() == emojiButton)
        {
            emojiFrame.setVisible(true);
        }
        else if(actionEvent.getSource() == cancelButton)
        {
            toMessageMode();
        }
        else if(actionEvent.getSource() == operationButton)
        {
            performOperation(inputText.getText());
            toMessageMode();
        }
    }

    /**
     * perform the "send"/"edit" operation when the button is pressed
     * @param typedText
     */
    public void performOperation(String typedText)
    {
        if(typedText.length() == 0)
            return;
        if(typedText.length() >= 80)
        {
            JOptionPane.showMessageDialog(this, "Message length over 80, that's too long!", "LengthLimit Exceeded", JOptionPane.PLAIN_MESSAGE);
        }
        try
        {
            if(operationMode == 0) //message
            {
                client.sendMessage(typedText);
            }
            else if(operationMode == 1) //quotation
            {
                client.sendQuotaion(typedText, selectedMessage.getId());
            }
            else if(operationMode == 2) //edit
            {
                client.sendEdit(selectedMessage.getId(), typedText);
            }
        }
        catch(IOException e)
        {
            errorNotification("It seems we have lost connection with the server.", "Connection Error");
        }
    }

    /** change text and label of the GUI when operationMode switches */
    public void toOperationMode(int modeCode, String operationText, Boolean cancelEnabelde, String initialText, Message selected, String selectedPre)
    {
        operationMode = modeCode;
        selectedMessage = selected;
        selectedLabel.setText(selected == null ? "" : selected.toOneLine());
        selectedLabel.setText(selectedPre + selectedLabel.getText());
        inputText.setText(initialText);
        cancelButton.setEnabled(cancelEnabelde);
        operationButton.setText(operationText);
    }

    public void toMessageMode()
    {
        toOperationMode(0, "send", false, "", null, "");
    }

    public void toQuotationMode(Message selected)
    {
        toOperationMode(1, "send", true , "", selected, "quoting: ");
    }
    
    public void toEditMode(Message selected)
    {
        toOperationMode(2, "edit", true , selected.getText(), selected, "editing: ");
    }

    /**
     * display a new message in the scrollPane
     * @param newMessage
     */
    public void newMessageDisplay(Message newMessage)
    {
        MessageDisplay newMessageDisplay = new MessageDisplay(newMessage);
        newMessageDisplay.setBounds(0+2, MessageDisplayCount*80+2, 1047-4, 80-4);
        ++MessageDisplayCount;
        displayPanel.setPreferredSize(new Dimension(1047, 80*MessageDisplayCount));
        displayPanel.add(newMessageDisplay);
        messageDisplays.put(newMessage.getId(), newMessageDisplay);
    }

    /**
     * update the display of a message when it's changed
     * @param message
     */
    public void updateMessageDisplay(Message message)
    {
        messageDisplays.get(message.getId()).update();
    }
    
    /** the unit container to display a single message */
    class MessageDisplay extends JPanel implements ActionListener
    {
        public Message message;
        private JLabel messageLabel;
        private JButton selectButton;
        private static String[] operationsSelf = {"Quote", "Edit", "Retreat", "Cancel"};
        private static String[] operationsOther = {"Quote", "Cancel"};

        public MessageDisplay(Message message)
        {
            this.message = message;
            this.setLayout(new BorderLayout());
            this.messageLabel = new JLabel();
            this.update();
            this.add(this.messageLabel, BorderLayout.CENTER);
            this.selectButton = new JButton("select");
            this.selectButton.addActionListener(this);
            this.selectButton.setFocusable(false);
            this.add(this.selectButton, BorderLayout.EAST);
        }

        /** update the content of the message */
        public void update()
        {
            this.messageLabel.setText(message.toHTML());
        }

        public void actionPerformed(ActionEvent actionEvent)
        {
            int response;
            if(this.message.getSenderName().equals(username))
            {
                response =  JOptionPane.showOptionDialog(this, "Choose your operation:", "Message Selected", 
                                                         JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, operationsSelf, -1);
            }
            else
            {
                response = JOptionPane.showOptionDialog(this, "Choose your operation:", "Message Selected", 
                                                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, operationsOther, -1);
                if(response == 1) response = 3;
            }
            if(response == 0) //quote
            {
                toQuotationMode(this.message);
            }
            else if(response == 1) //edit
            {
                toEditMode(this.message);
            }
            else if(response == 2) //retreat
            {
                try
                {
                    client.sendRetreat(this.message.getId());
                }
                catch(IOException e)
                {
                    errorNotification("It seems we have lost connection with the server.", "Connection Error");
                }
            }
        }
    }

    /**
     * show user some normal message
     * @param message
     * @param title
     */
    public void normalNotification(String message, String title)
    {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * show user some error message and then close the whole program
     * @param message
     * @param title
     */
    public void errorNotification(String message, String title)
    {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
        close();
    }
}
