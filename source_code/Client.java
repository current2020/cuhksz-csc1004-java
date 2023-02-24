import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

class Receiver extends Thread
{
    Scanner input;
    String command;
    boolean isRunning;

    public Receiver(Scanner input)
    {
        this.input = input;
        this.isRunning = true;
    }

    private void handle()
    {
        command = input.nextLine();
        if(command.equals("QUIT"))
        {

        }
        if(command.equals("MSG"))
        {

        }
        if(command.equals("EDIT"))
        {

        }
        if(command.equals("RETR"))
        {
            
        }
    }

    @Override
    public void run()
    {
        while(isRunning)
            handle();
    }
}

class LocalMessageList
{
    public final Object LOCK;
    private static int MAXN = 5000;
    private Message[] localMessageList;
    private String cache;
    private int top;


    public LocalMessageList()
    {
        this.LOCK = new Object();
        this.localMessageList = new Message[MAXN];
        this.cache = "";
        this.top = 0;
    }
    
    public String readCache()
    {
        String cache = this.cache;
        this.cache = "";
        return cache;
    }

    private void writeCache(Message newMessage)
    {
        if(cache.charAt(0) == '!')
            return;
        cache += newMessage.senderName + " " + newMessage.getSendingTime() + "\n";
        cache += newMessage.getContent() + "\n\n";
    }

    public void cacheNotification(String notification)
    {
        cache =  "!\n" + notification;
    }

    public void addNewMessage(Message newMessage)
    {
        localMessageList[top] = newMessage;
        top = (top + 1) % MAXN;
        writeCache(newMessage);
    }

    private int find(String username, int stepbacks)
    {
        int cur = (top - 1 + MAXN) % MAXN, counter = 0;
        while(counter < stepbacks)
        {
            if(localMessageList[cur].senderName.equals(username))
                ++counter;
            --cur;
            cur = cur < 0 ? cur + MAXN : cur;
        }
        return (cur + 1) % MAXN;
    }

    public void editMessage(String username, int stepbacks, String text)
    {
        int messageIndex = find(username, stepbacks);
        localMessageList[messageIndex].edit(text);
        cacheNotification("CHECK");
    }

    public void retreatMessage(String username, int stepbacks)
    {
        int messageIndex = find(username, stepbacks);
        localMessageList[messageIndex].retreat();
        cacheNotification("CHECK");
    }
}

public class Client
{
    Socket socket;
    Scanner input;
    PrintWriter output;
    String command;

    public void connect(String serverName, int port) throws IOException
    {
        this.socket = new Socket(serverName, port);
        this.input = new Scanner(socket.getInputStream());
        this.output = new PrintWriter(socket.getOutputStream());
    }

    public int register(String username, String password)
    {
        /* try to register such a user
         * return -1 stands for hostserver disconnected
         * return 0  stands for registered successfully
         * return 1  stands for userNumberLimit exceeded
         * return 11 stands for username being too short
         * return 12 stands for username being too long
         * return 13 stands for username syntax error
         * return 14 stands for username occuppied
         * return 21 stands for password being too short
         * return 22 stands for password being too long
         * return 23 stands for password syntax error
         */
        output.println("REGISTER");
        output.println(username);
        output.println(password);
        command = input.nextLine();
        if(command.equals("QUIT"))
            return -1;
        return Integer.parseInt(input.nextLine());
    }

    public int login(String username, String password)
    {
        /* a user tries to log in
         * return -1 stands for hostserver disconnected
         * return 0 stands for login successfully
         * return 1 stands for username invalid
         * return 2 stands for username does not exist
         * return 3 stands for password incorrect
         */
        output.println("LOGIN");
        output.println(username);
        output.println(password);
        command = input.nextLine();
        if(command.equals("QUIT"))
            return -1;
        return Integer.parseInt(input.nextLine());
    }

    public void startReceiver()
    {

    }

    public void snedTextMessage(String text)
    {

    }

    public boolean retreatMessage()
    {
        return true;
    }

    public boolean editMessage()
    {
        return true;
    }

    public String[] askForChats()
    {
        return new String[1];
    }
}
