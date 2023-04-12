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
                System.out.println("Connection success!");
                break;
            }
            catch(IOException e)
            {
                e.printStackTrace();
                System.out.println("Connection Failed...");
            }
            catch(ServerException e)
            {
                System.out.println(e.getErrorMessage());
            }
        }

        while(true)
        {
            command = input.nextLine();
            if(command.equals("register"))
            {
                String username, password;
                System.out.print("username: ");
                username = input.nextLine();
                System.out.print("password: ");
                password = input.nextLine();
                try
                {
                    client.register(username, password);
                    System.out.println("register success!");
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                    input.close();
                    return;
                }
                catch(LoginException e)
                {
                    System.out.print(e.getErrorMessage());
                }
                catch(ServerException e)
                {
                    System.out.print(e.getErrorMessage());
                    input.close();
                    return;
                }
            }
            else if(command.equals("login"))
            {
                String username, password;
                System.out.print("username: ");
                username = input.nextLine();
                System.out.print("password: ");
                password = input.nextLine();
                try
                {
                    client.login(username, password);
                    System.out.println("login success!");
                    break;
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                    input.close();
                    return;
                }
                catch(LoginException e)
                {
                    System.out.print(e.getErrorMessage());
                }
                catch(ServerException e)
                {
                    System.out.print(e.getErrorMessage());
                    input.close();
                    return;
                }
            }
        }

        while(true)
        {
            command = input.nextLine();
            try
            {
                if(command.equals("message"))
                {
                    System.out.print("text: ");
                    String text = input.nextLine();
                    client.sendMessage(text);
                }
                else if(command.equals("quotation"))
                {
                    System.out.print("text: ");
                    String text = input.nextLine();
                    System.out.print("targetId: ");
                    int targetId = Integer.parseInt(input.nextLine());
                    client.sendQuotaion(text, targetId);
                }
                else if(command.equals("edit"))
                {
                    System.out.print("targetId: ");
                    int targetId = Integer.parseInt(input.nextLine());
                    System.out.print("text: ");
                    String text = input.nextLine();
                    client.sendEdit(targetId, text);
                }
                else if(command.equals("retreat"))
                {
                    System.out.print("targetId: ");
                    int targetId = Integer.parseInt(input.nextLine());
                    client.sendRetreat(targetId);
                }
                else if(command.equals("chats"))
                {
                    for(Message msg: client.chatQuery(10))
                    {
                        if(msg == null)
                            continue;
                        System.out.println(msg.toStringWithId());
                    }
                }
                else if(command.equals("notification"))
                {
                    System.out.println(client.readNotification());
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
                input.close();
                return;
            }
        }
    }
}
