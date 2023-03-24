import java.net.Socket;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

class Notification
{
    String text;
    Object notificationLock = new Object();

    public void newNotification(String text)
    {
        synchronized(notificationLock)
        {
            this.text = text;
        }
    }

    public String readNotification()
    {
        synchronized(notificationLock)
        {
            if(text == null)
                return "";
            String str = text;
            text = null;
            return str;
        }
    }
}

class Receiver extends Thread
{
    private MessageList messageList;
    private Notification notification;
    private BufferedReader input;
    private boolean isRunning;

    public Receiver(MessageList messageList, Notification notification, BufferedReader input)
    {
        this.messageList = messageList;
        this.notification = notification;
        this.input = input;
        this.isRunning = true;
        this.setDaemon(true);
    }

    @Override
    public void run()
    {
        String command;
        while(isRunning)
        {
            try
            {
                command = input.readLine();
                if(command.equals("~MESSAGE"))
                    handleMessage();
                else if(command.equals("~QUOTATION"))
                    handleQuotation();
                else if(command.equals("~EDIT"))
                    handleEdit();
                else if(command.equals("~RETREAT"))
                    handleRetreat();
                else if(command.equals("~REPLY"))
                    handleReply();
                else if(command.equals("~QUIT"))
                    handleQuit();
            }
            catch(IOException e)
            {
                notification.newNotification("It seems we have lost connection to the server.");
                isRunning = false;
            }
        }
    }

    private void handleMessage() throws IOException
    {
        String senderName = input.readLine();
        int id = Integer.parseInt(input.readLine());
        String text = input.readLine();
        messageList.addNewMessage(new Message(senderName, id, text));
    }

    private void handleQuotation() throws IOException
    {
        String senderName = input.readLine();
        int id = Integer.parseInt(input.readLine());
        String text = input.readLine();
        String targetSenderName = input.readLine();
        int targetId = Integer.parseInt(input.readLine());
        Message quotation = messageList.find(targetSenderName, targetId);
        if(quotation == null)
            quotation = messageList.unknownMessage;
        messageList.addNewMessage(new Message(senderName, id, text, quotation));
    }

    private void handleEdit() throws IOException
    {
        String targetSenderName = input.readLine();
        int targetId = Integer.parseInt(input.readLine());
        String text = input.readLine();
        Message target = messageList.find(targetSenderName, targetId);
        if(target == null) return;
        target.edit(text);
    }

    private void handleRetreat() throws IOException
    {
        String targetSenderName = input.readLine();
        int targetId = Integer.parseInt(input.readLine());
        Message target = messageList.find(targetSenderName, targetId);
        if(target == null) return;
        target.retreat();
    }

    private void handleReply() throws IOException
    {
        String reply = input.readLine();
        if(reply.equals("fail"))
            notification.newNotification("edit/retreat request rejected.");
    }

    private void handleQuit() throws IOException
    {
        notification.newNotification("server is quiting...");
        isRunning = false;
    }
}

public class Client
{
    private Socket socket;
    private BufferedReader input;
    private BufferedWriter output;
    private MessageList messageList;
    private Notification notification;
    private String username;

    public void connect(String serverName, int port) throws IOException, ServerException
    {
        this.socket = new Socket(serverName, port);
        this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), StandardCharsets.UTF_8));
        this.output = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream(), StandardCharsets.UTF_8));
        String command;
        command = input.readLine();
        if(command.equals("~QUIT"))
            throw ServerException.serverQuiting();
        if(!command.equals("~CONNECT"))
            throw ServerException.unknownError();
        String reply = input.readLine();
        if(reply.equals("fail"))
            throw ServerException.serverBusy();
    }

    private void send(String command, String ... messages) throws IOException
    {
        output.write(command);
        output.newLine();
        for(String message: messages)
        {
            output.write(message);
            output.newLine();
        }
        output.flush();
    }

    public void register(String username, String password) throws IOException, LoginException, ServerException
    {
        send("~REGISTER", username, password);
        String command = input.readLine();
        if(command.equals("~QUIT"))
            throw ServerException.serverQuiting();
        if(!command.equals("~REGISTER"))
            throw ServerException.unknownError();
        int errorCode = Integer.parseInt(input.readLine());
        if(errorCode > 0)
            throw new LoginException(errorCode);
    }

    public void login(String username, String password) throws IOException, LoginException, ServerException
    {
        send("~LOGIN", username, password);
        String command = input.readLine();
        if(command.equals("~QUIT"))
            throw ServerException.serverQuiting();
        if(!command.equals("~LOGIN"))
            throw ServerException.unknownError();
        int errorCode = Integer.parseInt(input.readLine());
        if(errorCode > 0)
            throw new LoginException(errorCode);
        start(username);
    }

    private void start(String username)
    {
        this.username = username;
        this.messageList = new MessageList();
        this.notification = new Notification();
        (new Receiver(messageList, notification, input)).start();
    }

    public void sendMessage(String text) throws IOException
    {
        send("~MESSAGE", text);
    }

    public void sendQuotaion(String text, String targetSenderName, int targetId) throws IOException
    {
        send("~QUOTATION", text, targetSenderName, Integer.toString(targetId));
    }

    public void sendEdit(int targetId, String text) throws IOException
    {
        send("~EDIT", Integer.toString(targetId), text);
    }

    public void sendRetreat(int targetId) throws IOException
    {
        send("~RETREAT", Integer.toString(targetId));
    }

    public Message[] chatQuery(int number)
    {
        return messageList.chatQuery(number);
    }

    public String readNotification()
    {
        return notification.readNotification();
    }

    public String getUsername()
    {
        return this.username;
    }
}
