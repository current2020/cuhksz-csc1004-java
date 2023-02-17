import java.util.*;

class Trie
{
    //todo

    Trie()
    {
        //todo
    }

    void insert(String str, int info)
    {
        //todo
    }

    int find(String str)
    {
        //todo
        return 0;
    }
}

public class Server
{
    static class User
    {
        String name;
        String password;

        User(String name, String password)
        {
            this.name = name;
            this.password = password;
        }
    }

    static class UserList
    {
        static int MAXN = 500;
        static int top = 0;
        static User[] userlist = new User[MAXN];

        static Trie namepool = new Trie();

        static boolean nameAccess(String name)
        {
            if(namepool.find(name) == 1)
                return false;
            return true;
        }

        static String register(String name, String password)
        {
            if(top == MAXN)
                return "Maxinum registered user amount reached.";
            if(!nameAccess(name))
                return "Name occupied! Pick Another Name";
            userlist[top++] = new User(name, password);
            return "Successfully registered!"; 
        }

        static String getName(int id)
        {
            if(userlist[id] == null)
                return "UserNotFound";
            return userlist[id].name;
        }
    }

    static class Message
    {
        int senderID;
        Date sendinTime;
        String content;

        Message(int sender, String msg)
        {
            senderID = sender;
            content = msg;
            sendinTime = new Date();
        }

        void printMessage()
        {
            System.out.print(UserList.getName(senderID) + ' ');
            System.out.println(sendinTime.toString());
            System.out.println(content);
            System.out.print("\n");
        }
    }

    static class MessageList
    {
        static int MAXN = 500;
        static Message[] messagelist = new Message[MAXN];
        static int top = 0;

        static void receive(int sender, String msg)
        {
            messagelist[top++] = new Message(sender, msg);
            top %= MAXN;
        }

        static void askForChatHistory(int num)
        {
            for(int idx = (top - num + MAXN) % MAXN; idx != top; idx = (idx + 1 + MAXN) % MAXN)
            {
                if(messagelist[idx] == null)
                    continue;
                messagelist[idx].printMessage();
            }
        }
    }

    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        String cmd;
        while(true)
        {
            cmd = input.nextLine();
            if(cmd.equals("quit"))
            {
                input.close();
                return;
            }
            else if(cmd.equals("register"))
            {
                System.out.print("Name: ");
                String name = input.nextLine();
                System.out.print("Password: ");
                String password = input.nextLine();
                System.out.println(UserList.register(name, password));
                System.out.print("\n");
            }
            else if(cmd.equals("sendmsg"))
            {
                int senderID = input.nextInt();
                String msg = input.nextLine();
                MessageList.receive(senderID, msg);
                System.out.println("Successfully sent");
                System.out.print("\n");
            }
            else if(cmd.equals("checkhis"))
            {
                int num = input.nextInt();
                input.nextLine();
                MessageList.askForChatHistory(num);
                System.out.print("\n");
            }
            else
            {
                System.out.println("Wrong Command. Type Again!");
                System.out.print("\n");
            }
        }
    }
}
