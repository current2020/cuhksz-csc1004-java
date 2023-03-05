import java.io.IOException;
import java.util.Scanner;

public class ServerConsole
{
    private static Server server = new Server();

    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        String command;
        int port;
        while(true)
        {
            System.out.print("port: ");
            port = Integer.parseInt(input.nextLine());
            try
            {
                server.start(port);
                break;
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        System.out.println("Server is running...");
        System.out.println("type \"quit\" to stop");
        while(true)
        {
            command = input.nextLine();
            if(command == "quit")
            {
                server.quit();
                break;
            }
        }
        input.close();
    }
}
