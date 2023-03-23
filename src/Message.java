import java.util.Date;

public class Message
{
    private static long editTimeLimitMillisecond = 120000;
    public String senderName;
    private int id;
    private String text;
    private Message quotation;
    private Date sendingTime;
    public boolean isRetreated;
    public boolean isEdited;

    public Message(String senderName, int id, String text)
    {
        this.isRetreated = false;
        this.isEdited = false;
        this.senderName = senderName;
        this.id = id;
        this.text = text;
        this.sendingTime = new Date();
    }

    public Message(String senderName, int id, String text, Message quotation)
    {
        this(senderName, id, text);
        this.quotation = quotation;
    }

    /** USE FOR SERVER ONLY */
    public Message(int id)
    {
        this.isRetreated = true;
        this.id = id;
        this.sendingTime = new Date();
    }

    public boolean is(String senderName, int id)
    {
        if(this.senderName.equals(senderName) && this.id == id)
            return true;
        return false;
    }

    /** return a shorter string within lengthLimit */
    private static String fix(String str, int lengthLimit)
    {
        lengthLimit = lengthLimit > 3 ? lengthLimit : 3;
        if(str.length() <= lengthLimit)
            return str;
        return str.substring(0, lengthLimit-3) + "...";
    }

    /** for test only */
    public String toStringWithId()
    {
        return this.toString() + "\n" + this.id;
    }

    /** turn this message into a string for display, with no \n at the end */
    public String toString()
    {
        String res = senderName + " " + sendingTime.toString() + "\n";
        if(this.isRetreated)
            return res + "[Retreated]";
        if(this.quotation != null)
            res += ">>> " + quotation.toOneLine() + "\n";
        res += text;
        if(this.isEdited)
            res += "\n[Edited]";
        return res;
    }

    /** turn this message into a short one line string for being quoted */
    public String toOneLine()
    {
        String res = senderName + ": ";
        if(isRetreated)
            return res + "[Retreated]";
        res += fix(text, 32);
        if(isEdited)
            res += "[Edited]";
        return res;
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
