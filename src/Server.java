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
    public boolean isRunning;
    private static int clientNumberLimit = 100;
    public ThreadServer[] threadServers;
    private int top;

    public ServerState()
    {
        isRunning = true;
        threadServers = new ThreadServer[clientNumberLimit];
        top = 0;
    }

    public boolean addClient(ThreadServer newThreadServer)
    {
        if(top == clientNumberLimit)
            return false;
        threadServers[top++] = newThreadServer;
        return true;
    }
}

class ThreadServer extends Thread
{
    public Object outputLock = new Object();
    public boolean isConnected;
    private Socket socket;
    private BufferedReader input;
    public BufferedWriter output;
    private ServerState serverState;

    public ThreadServer(Socket socket, ServerState serverState)
    {
        this.serverState = serverState;
        this.socket = socket;
        isConnected = true;
        try
        {
            this.input = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            this.output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        }
        catch(IOException e)
        {
            close();
        }
    }

    @Override
    public void run()
    {
        String command;
        while(isConnected&&serverState.isRunning)
        {
            try
            {
                command = input.readLine();
                if(command == "SAY") handleSay();
            }
            catch(IOException e)
            {
                close();
            }
        }
        if(isConnected) quit();
        close();
    }

    private void close()
    {
        try {this.socket.close();}
        catch(IOException e) {}
        isConnected = false;
    }

    private void quit()
    {
        try
        {
            synchronized(outputLock)
            {
                output.write("QUIT");
                output.newLine();
                output.flush();
            }
        }
        catch(IOException e) {}
    }

    private void handleSay() throws IOException //for test
    {
        String text = input.readLine();
        if(text == "xxx")
        {
            synchronized(outputLock)
            {
                output.write("UNK");
                output.newLine();
                output.flush();
            }
            return;
        }
        synchronized(outputLock)
        {
            output.write("REPLY");
            output.newLine();
            output.write("I have received.");
            output.newLine();
            output.flush();
        }
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
    }

    @Override
    public void run()
    {
        while(serverState.isRunning)
        {
            Socket newSocket;
            try {newSocket = serverSocket.accept();}
            catch(IOException e) {continue;}
            ThreadServer newThreadServer = new ThreadServer(newSocket, serverState);
            serverState.addClient(newThreadServer);
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
        serverState.isRunning = false;
    }
}
