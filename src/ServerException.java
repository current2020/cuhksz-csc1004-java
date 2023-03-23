public class ServerException extends RuntimeException
{
    private int errorCode;

    static private String errorMessages[] = 
    {
        null, //0
        "Server too busy...", //1
        "Server is quiting...", //2
        "Unknown error" //3
    };

    public ServerException(int errorCode)
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

    static public ServerException serverBusy()      { return new ServerException(1); }
    static public ServerException serverQuiting()   { return new ServerException(2); }
    static public ServerException unknownError()    { return new ServerException(3); }
}
