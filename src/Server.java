import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

class ServerState
{
    private static int clientNumberLimit = 100;
    private int top;
    public ThreadServer[] threadServers;
    public UserList userList;
    public ChatHistory chatHistory;

    public ServerState()
    {
        threadServers = new ThreadServer[clientNumberLimit];
        top = 0;
        userList = new UserList();
        chatHistory = new ChatHistory();
    }

    public void close()
    {
        for(ThreadServer client: threadServers)
        {
            if(client == null) continue;
            if(!client.isConnected()) continue;
            client.quit();
        }
        chatHistory.close();
    }

    public boolean addClient(ThreadServer newThreadServer)
    {
        for(int i = 0; i < top; ++i)
        {
            if(threadServers[i].isConnected())
                continue;
            threadServers[i] = newThreadServer;
            return true;
        }
        if(top == clientNumberLimit)
            return false;
        threadServers[top++] = newThreadServer;
        return true;
    }
}

class ThreadServer extends Thread
{
    private Object outputLock = new Object();
    private Socket socket;
    private BufferedReader input;
    private BufferedWriter output;
    private ServerState serverState;
    private boolean isConnected;
    private boolean isLoggedIN;
    private User user;
    private MessageList messageList;

    public ThreadServer(Socket socket, ServerState serverState)
    {
        this.serverState = serverState;
        this.socket = socket;
        isConnected = true;
        isLoggedIN = false;
        try
        {
            this.input = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            this.output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        }
        catch(IOException e) {close();}
    }
    
    @Override
    public void run()
    {
        String command;
        try
        {
            while(!isLoggedIN)
            {
                command = input.readLine();
                if(command.equals("~REGISTER"))
                    handleRegister();
                else if(command.equals("~LOGIN"))
                    handleLogin();
            }
            while(isConnected)
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
            }
        }
        catch(IOException e) {close();}
    }

    public boolean isConnected() { return this.isConnected; }

    public boolean isLoggedIN() { return this.isLoggedIN; }
    
    public void close()
    {
        try {this.socket.close();}
        catch(IOException e) {}
        isConnected = false;
        isLoggedIN = false;
    }
    
    public void quit()
    {
        send("QUIT");
        close();
    }

    private void start(User user)
    {
        serverState.chatHistory.restore(this);
        isLoggedIN = true;
        this.user = user;
        this.messageList = new MessageList();
    }

    public void send(String command, String ... messages)
    {
        try
        {
            synchronized(outputLock)
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
        }
        catch(IOException e) {close();}
    }

    private void broadCast(String command, String ... messages)
    {
        for(ThreadServer threadServer: serverState.threadServers)
        {
            if(threadServer == null) return;
            if(!threadServer.isLoggedIN) continue;
            threadServer.send(command, messages);
        }
    }

    private void handleRegister() throws IOException
    {
        String username = input.readLine();
        String password = input.readLine();
        try
        {
            serverState.userList.registerAttempt(username, password);
        }
        catch(LoginException e)
        {
            send("~REGISTER", Integer.toString(e.getErrorcode()));
            return;
        }
        send("~REGISTER", "0");
    }

    private void handleLogin() throws IOException
    {
        String username = input.readLine();
        String password = input.readLine();
        User user;
        try
        {
            user = serverState.userList.loginAttempt(username, password);
        }
        catch(LoginException e)
        {
            send("~LOGIN", Integer.toString(e.getErrorcode()));
            return;
        }
        send("~LOGIN", "0");
        start(user);
    }


    private void handleMessage() throws IOException
    {
        String text = input.readLine();
        int messageId = serverState.chatHistory.newMessage(user.getUsername(), text, 0);
        messageList.addNewMessage(new Message(messageId));
        broadCast("~MESSAGE", Integer.toString(messageId), user.getUsername(), text);
    }

    private void handleQuotation() throws IOException
    {
        String text = input.readLine();
        int targetId = Integer.parseInt(input.readLine());
        int messageId = serverState.chatHistory.newMessage(user.getUsername(), text, targetId);
        messageList.addNewMessage(new Message(messageId));
        broadCast("~QUOTATION", Integer.toString(messageId), user.getUsername(), text, Integer.toString(targetId));
    }

    private void handleEdit() throws IOException
    {
        int targetId = Integer.parseInt(input.readLine());
        String text = input.readLine();
        Message message = messageList.find(targetId);
        if(message == null || (!message.withinTime()))
        {
            send("~REPLY", "fail");
            return;
        }
        send("~REPLY", "success");
        broadCast("~EDIT", Integer.toString(targetId), text);
        serverState.chatHistory.editMessage(targetId, text);
    }

    private void handleRetreat() throws IOException
    {
        int targetId = Integer.parseInt(input.readLine());
        Message message = messageList.find(targetId);
        if(message == null || (!message.withinTime()))
        {
            send("~REPLY", "fail");
            return;
        }
        send("~REPLY", "success");
        broadCast("~RETREAT", Integer.toString(targetId));
        serverState.chatHistory.retreatMessage(targetId);
    }
}

class Receptionist extends Thread
{
    private ServerSocket serverSocket;
    private ServerState serverState;

    public Receptionist(ServerSocket serverSocket, ServerState serverState)
    {
        this.serverSocket = serverSocket;
        this.serverState = serverState;
        this.setDaemon(true);
    }

    @Override
    public void run()
    {
        while(true)
        {
            Socket newSocket;
            try {newSocket = serverSocket.accept();}
            catch(IOException e) {continue;}
            ThreadServer newThreadServer = new ThreadServer(newSocket, serverState);
            if(!newThreadServer.isConnected()) continue;
            if(serverState.addClient(newThreadServer))
            {
                newThreadServer.send("~CONNECT", "success");
            }
            else
            {
                newThreadServer.send("~CONNECT", "fail");
                newThreadServer.close();
            }
            newThreadServer.start();
        }
    }
}

public class Server
{
    private ServerSocket serverSocket;
    private ServerState serverState;
    private Receptionist receptionist;

    public void start(int port) throws IOException
    {
        serverState = new ServerState();
        serverSocket = new ServerSocket(port);
        receptionist = new Receptionist(serverSocket, serverState);
        receptionist.start();
    }

    public void quit()
    {
        serverState.close();
    }
}
