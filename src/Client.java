import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

class LocalMessageList
{
    public final Object LOCK;
    private static int MAXN = 5000;
    private Message[] localMessageList;
    private int top;

    public LocalMessageList()
    {
        this.LOCK = new Object();
        this.localMessageList = new Message[MAXN];
        this.top = 0;
    }

    public void addNewMessage(Message newMessage)
    {
        localMessageList[top] = newMessage;
        top = (top + 1) % MAXN;
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
    }

    public void retreatMessage(String username, int stepbacks)
    {
        int messageIndex = find(username, stepbacks);
        localMessageList[messageIndex].retreat();
    }
}

class Receiver extends Thread
{
    Scanner input;
    String command;
    boolean isRunning;
    LocalMessageList localMessageList;

    public Receiver(Scanner input, LocalMessageList localMessageList)
    {
        this.input = input;
        this.isRunning = true;
        this.localMessageList = localMessageList;
    }

    private void quitRequest()
    {
        synchronized(localMessageList.LOCK)
        {
            localMessageList.cacheNotification("QUIT");
        }
    }

    private void messageRequest()
    {
        String senderName = input.nextLine();
        String text = input.nextLine();
        synchronized(localMessageList.LOCK)
        {
            localMessageList.addNewMessage(new Message(senderName, text));
        }
    }

    private void editRequest()
    {
        String username = input.nextLine();
        int stepbacks = Integer.parseInt(input.nextLine());
        String text = input.nextLine();
        synchronized(localMessageList.LOCK)
        {
            localMessageList.editMessage(username, stepbacks, text);
        }
    }

    private void retreatRequest()
    {
        String username = input.nextLine();
        int stepbacks = Integer.parseInt(input.nextLine());
        synchronized(localMessageList.LOCK)
        {
            localMessageList.retreatMessage(username, stepbacks);
        }
    }

    private void handle()
    {
        command = input.nextLine();
        if(command.equals("QUIT"))
            quitRequest();
        if(command.equals("MSG"))
            messageRequest();
        if(command.equals("EDIT"))
            editRequest();
        if(command.equals("RETR"))
            retreatRequest();
    }

    @Override
    public void run()
    {
        while(isRunning)
            handle();
    }
}

public class Client
{
    Socket socket;
    Scanner input;
    PrintWriter output;
    String command;
    LocalMessageList localMessageList;
    Receiver receiver;

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

    public void start()
    {
        localMessageList = new LocalMessageList();
        receiver = new Receiver(input, localMessageList);
        receiver.start();
    }

    private boolean isTextValid(String text)
    {
        for(char ch: text.toCharArray())
            if(ch == '\n')
                return false;
        return true;
    }

    public boolean snedTextMessage(String text)
    {
        /* cannot send TEXT contain '\n' */
        if(!isTextValid(text)) return false;
        output.println("MSG");
        output.println(text);
        return true;
    }

    public boolean retreatMessage()
    {
        /* cannot retreat messages too long ago */
        
        return true;
    }

    public boolean editMessage()
    {
        return true;
    }

    public String readCache()
    {

    }

    public String askForChats()
    {

    }
}
