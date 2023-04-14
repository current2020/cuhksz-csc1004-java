public class MessageList
{
    public final Object LOCK;
    private static int MAXN = 5000;
    public Message unknownMessage;
    private Message[] messageList;
    private int top;

    public MessageList()
    {
        this.LOCK = new Object();
        this.messageList = new Message[MAXN];
        this.top = 0;
        unknownMessage = new Message(0, "SERVER", "[unknown message]");
    }

    public void addNewMessage(Message newMessage)
    {
        messageList[top] = newMessage;
        top = top+1 == MAXN ? 0 : top+1;
    }

    /** return the newest number-th messages in a list */
    public Message[] chatQuery(int number)
    {
        Message[] res = new Message[number];
        number = number > MAXN ? MAXN : number;
        int cur = top < number ? top-number+MAXN : top-number;
        int resTop = 0;
        while(cur != top)
        {
            if(messageList[cur] != null)
                res[resTop++] = messageList[cur];
            cur = cur+1 == MAXN ? 0 : cur+1;
        }
        return res;
    }

    /** return the certain message, return null if not found */
    public Message find(int id)
    {
        int cur = top == 0 ? MAXN-1 : top-1;
        while(cur != top && messageList[cur] != null)
        {
            if(messageList[cur].getId() == id)
                return messageList[cur];
            cur = cur == 0 ? MAXN-1 : cur-1;
        }
        return null;
    }
}
