import java.io.IOException;
import java.net.Socket;

class Receiver extends Thread
{
    
}

class Sender extends Thread
{

}



public class Client
{
    Socket socket;

    public void connect(String serverName, int port) throws IOException
    {
        this.socket = new Socket(serverName, port);
    }
    
    public int register(String username, String password)
    {
        return 0;
    }

    public int login(String username, String password)
    {
        return 0;
    }

    public void start()
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
