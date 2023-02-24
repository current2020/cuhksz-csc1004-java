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

    private void edit(String text)
    {
        this.isEdited = true;
        this.text = text;
    }

    private void retreat()
    {
        this.isRetreated = true;
    }

    public int editAttempt(String text)
    {
        /* return 0 stands for edit successfully
         * return 1 stands for allowed edit time exceeded
         * return 2 stands for this is not a text message
         */
        if(messageType != 0)
            return 2;
        Date currenDate = new Date();
        if(currenDate.getTime() - this.sendingTime.getTime() > editTimeLimitMillisecond)
            return 1;
        this.edit(text);
        return 0;
    }

    public int retreatAttempt()
    {
        /* return 0 stands for retreat successfully
         * return 1 stands for allowed edit time exceeded
         */
        Date currenDate = new Date();
        if(currenDate.getTime() - this.sendingTime.getTime() > editTimeLimitMillisecond)
            return 1;
        this.retreat();
        return 0;
    }

    public String getSendingTime()
    {
        return sendingTime.toString();
    }

    public String getContent()
    {
        if(isRetreated)
            return "";
        if(messageType == 0)
            return text;
        return "Not Supported Message Type";
    }

    public String getLabel()
    {
        if(isRetreated)
            return "[Retreated]";
        if(isEdited)
            return "[Edited]";
        return "";
    }

    public long getTime()
    {
        return this.sendingTime.getTime();
    }

    public void fixTime()
    {
        this.sendingTime = new Date();
    }
}
