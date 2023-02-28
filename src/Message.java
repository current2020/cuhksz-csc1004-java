import java.util.Date;

public class Message
{
    public String senderName;
    private Date sendingTime;
    private String text;
    private int messageType;
    private static long editTimeLimitMillisecond = 120000;
    public boolean isRetreated;
    public boolean isEdited;

    public Message(String senderName, String text)
    {
        this.messageType = 0;
        this.isRetreated = false;
        this.isEdited = false;
        this.senderName = senderName;
        this.sendingTime = new Date();
        this.text = text;
    }

    public void edit(String text)
    {
        this.isEdited = true;
        this.text = text;
    }

    public void retreat()
    {
        this.isRetreated = true;
        this.text = null;
    }

    public boolean withinTime()
    {
        Date currenDate = new Date();
        if(currenDate.getTime() - this.sendingTime.getTime() > editTimeLimitMillisecond)
            return false;
        return true;
    }

    public String getSendingTime()
    {
        return sendingTime.toString();
    }

    public String getContent()
    {
        if(isRetreated)
            return "";
        if(messageType > 0)
            return "Not Supported Message Type";
        return text;
    }
}
