import java.io.IOException;
import java.util.Scanner;

public class ClientConsole
{
    private static Client client = new Client();

    public static void main(String[] args) 
    {
        Scanner input = new Scanner(System.in);
        String serverName;
        String command;
        int port;
        while(true)
        {
            System.out.print("ServerName: ");
            serverName = input.nextLine();
            System.out.print("port: ");
            port = Integer.parseInt(input.nextLine());
            try
            {
                client.connect(serverName, port);
                break;
            }
            catch(IOException e)
            {
                e.printStackTrace();
                System.out.println("Connection Failed...");
            }
        }
        while(true)
        {
            command = input.nextLine();
            if(command.equals("say"))
            {
                String msg;
                msg = input.nextLine();
                try
                {
                    client.say(msg);
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
            else if(command.equals("receive"))
            {
                try
                {
                    String msg;
                    msg = client.receive();
                    System.out.println(msg);
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
            else if(command.equals("quit"))
            {
                break;
            }
            else
            {
                System.out.println("Unknown Command:");
                System.out.println(command);
            }
        }
        input.close();
    }
}
