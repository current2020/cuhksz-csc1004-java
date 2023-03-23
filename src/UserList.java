public class UserList
{
    private Trie namepool;
    private int top;
    private static int userNumberLimit = 100;
    private static int usernameLengthLimit = 20, usernameLengthLimitMin = 3;
    private static int passwordLengthLimit = 16, passwordLengthLimitMin = 6;

    public UserList()
    {
        this.top = 0;
        int MAXN = UserList.userNumberLimit * UserList.usernameLengthLimit + 5;
        this.namepool = new Trie(MAXN);
    }

    private void register(String username, String password)
    {
        namepool.insert(username, new User(username, password));
    }

    public void registerAttempt(String username, String password) throws LoginException
    {
        if(top == userNumberLimit)
            throw LoginException.userNumberLimitExceeded();
        if(username.length() < usernameLengthLimitMin)
            throw LoginException.usernameTooShort();
        if(username.length() > usernameLengthLimit)
            throw LoginException.usernameTooLong();
        if(!User.isUsernameValid(username))
            throw LoginException.usernameSyntaxError();
        if(password.length() < passwordLengthLimitMin)
            throw LoginException.passwordTooShort();
        if(password.length() > passwordLengthLimit)
            throw LoginException.passwordTooLong();
        if(!User.isPasswordValid(password))
            throw LoginException.passwordSyntaxError();
        if(namepool.find(username) != null)
            throw LoginException.usernameOccupied();
        register(username, password);
    }

    public User loginAttempt(String username, String password) throws LoginException
    {
        if(username.length() < usernameLengthLimitMin)
            throw LoginException.usernameTooShort();
        if(username.length() > usernameLengthLimit)
            throw LoginException.usernameTooLong();
        if(!User.isUsernameValid(username))
            throw LoginException.usernameSyntaxError();
        User user = namepool.find(username);
        if(user == null)
            throw LoginException.usernameNotExist();
        if(!user.loginAttempt(password))
            throw LoginException.passwordIncorrect();
        return user;
    }
}
