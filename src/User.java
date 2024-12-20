public class User
{
    private String username;
    private String password;
    public int age;
    public String gender;
    public String address;

    public User(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, int age, String gender, String address)
    {
        this.username = username;
        this.password = password;
        this.age = age;
        this.gender = gender;
        this.address = address;
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
}
