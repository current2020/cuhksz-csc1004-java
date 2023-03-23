public class User
{
    private String username;
    private String password;
    private int messageId;

    public User(String username, String password, int messageId)
    {
        this.username = username;
        this.password = password;
        this.messageId = messageId;
    }

    public User(String username, String password)
    {
        this(username, password, 0);
    }

    public String getUsername()
    {
        return username;
    }

    private static boolean numberOrLetter(char ch)
    {
        if(ch >= 'a' && ch <= 'z')
            return true;
        if(ch >= 'A' && ch <= 'Z')
            return true;
        if(ch >= '0' && ch <= '9')
            return true;
        return false;
    }

    private static boolean isValidForUsername(char ch)
    {
        /* only a-z, A-Z, 0-9, _ and space is allowed for username
         */
        if(numberOrLetter(ch))
            return true;
        if(ch == '_')
            return true;
        if(ch == ' ')
            return true;
        return false;
    }

    public static boolean isUsernameValid(String username)
    {
        for(char ch: username.toCharArray())
        {
            if(!isValidForUsername(ch))
                return false;  
        }
        return true;
    }
    
    private static boolean isValidForPassword(char ch)
    {
        /* only a-z, A-Z, 0-9 and ! @ # $ % ^ & * are valid for password
         */
        if(numberOrLetter(ch))
            return true;
        if(ch == '!' || ch == '@' || ch == '#' || ch == '$')
            return true;
        if(ch == '%' || ch == '^' || ch == '&' || ch == '*')
            return true;
        return false;
    }

    public static boolean isPasswordValid(String password)
    {
        for(char ch: password.toCharArray())
        {
            if(!isValidForPassword(ch))
                return false;  
        }
        return true;
    }

    public boolean loginAttempt(String textPassword)
    {
        return this.password.equals(textPassword);
    }

    public int getNextMessageId()
    {
        return ++messageId;
    }
}
