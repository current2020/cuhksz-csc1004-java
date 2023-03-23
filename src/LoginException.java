public class LoginException extends RuntimeException
{
    private int errorCode;

    static private String errorMessages[] = 
    {
        null, //0
        "Username being too short", //1
        "Username being too long", //2
        "Username syntax error", //3
        "Password being too long", //4
        "Password being too short", //5
        "Password syntax error", //6
        "Username occupied", //7
        "Username does not exist", //8
        "Password incorrect", //9
        "SERVER: userNumberLimit exceeded" //10
    };

    public LoginException(int errorCode)
    {
        this.errorCode = errorCode;
    }

    public String getErrorMessage()
    {
        return errorMessages[this.errorCode];
    }

    public int getErrorcode()
    {
        return this.errorCode;
    }

    static public LoginException usernameTooShort()         { return new LoginException(1); }
    static public LoginException usernameTooLong()          { return new LoginException(2); }
    static public LoginException usernameSyntaxError()      { return new LoginException(3); }
    static public LoginException passwordTooShort()         { return new LoginException(4); }
    static public LoginException passwordTooLong()          { return new LoginException(5); }
    static public LoginException passwordSyntaxError()      { return new LoginException(6); }
    static public LoginException usernameOccupied()         { return new LoginException(7); }
    static public LoginException usernameNotExist()         { return new LoginException(8); }
    static public LoginException passwordIncorrect()        { return new LoginException(9); }
    static public LoginException userNumberLimitExceeded()  { return new LoginException(10);}
}
