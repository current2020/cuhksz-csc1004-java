public class MessageList
{
    public final Object LOCK;
    private static int MAXN = 5000;
    private Message[] messageList;
    private int top;

    public MessageList()
    {
        this.LOCK = new Object();
        this.messageList = new Message[MAXN];
        this.top = 0;
    }

    public void addNewMessage(Message newMessage)
    {
        messageList[top] = newMessage;
        top = top+1 == MAXN ? 0 : top+1;
    }

    public Message find(String username, int stepbacks)
    {
        int cur = (top - 1 + MAXN) % MAXN, counter = 0;
        while(counter < stepbacks)
        {
            if(messageList[cur].senderName.equals(username))
                ++counter;
            cur = cur == 0 ? MAXN : cur-1;
        }
        return messageList[(cur + 1) % MAXN];
    }

    public String chatQuery(int number)
    {
        Message msg;
        String res = "";
        number = number <= MAXN ? number : MAXN;
        int cur = (top - number + MAXN) % MAXN;
        while(cur != top)
        {
            msg = messageList[cur];
            if(msg == null) continue;
            res += msg.senderName + ' ' + msg.getSendingTime() + '\n';
            if(msg.isRetreated)
            {
                res += "[Retreated]\n";
            }
            else
            {
                res += msg.text + '\n';
                if(msg.isEdited)
                    res += "[Edited]\n";
            }
            res += '\n';
            cur = cur+1 == MAXN ? 0 : cur+1;
        }
        return res;
    }
}
