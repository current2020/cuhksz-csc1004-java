import java.util.Date;

public class Message
{
    private static long editTimeLimitMillisecond = 120000;
    public String senderName;
    public String text;
    private Date sendingTime;
    public boolean isRetreated;
    public boolean isEdited;

    public Message(String senderName, String text)
    {
        this.isRetreated = false;
        this.isEdited = false;
        this.senderName = senderName;
        this.sendingTime = new Date();
        this.text = text;
    }

    public String getSendingTime()
    {
        return sendingTime.toString();
    }
    
    public boolean withinTime()
    {
        Date currenDate = new Date();
        if(currenDate.getTime() - this.sendingTime.getTime() > editTimeLimitMillisecond)
            return false;
        return true;
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
}
