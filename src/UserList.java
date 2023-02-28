public class UserList
{
    private User[] userList;
    private int top;
    private static int userNumberLimit = 100;
    private static int usernameLengthLimit = 20, usernameLengthLimitMin = 3;
    private static int passwordLengthLimit = 16, passwordLengthLimitMin = 6;
    private Trie namepool;

    public UserList()
    {
        this.userList = new User[UserList.userNumberLimit];
        this.top = 0;
        int MAXN = UserList.userNumberLimit * UserList.usernameLengthLimit + 1;
        this.namepool = new Trie(MAXN);
    }

    private void register(String username, String password)
    {
        /* this is private
         * you should always register through registerAttempt()
         */
        namepool.insert(username, top);
        userList[top++] = new User(username, password);
    }

    public int registerAttempt(String username, String password)
    {
        /* try to register such a user
         * return 0  stands for registered successfully
         * return 1  stands for userNumberLimit exceeded
         * return 11 stands for username being too short
         * return 12 stands for username being too long
         * return 13 stands for username syntax error
         * return 14 stands for username occuppied
         * return 21 stands for password being too short
         * return 22 stands for password being too long
         * return 23 stands for password syntax error
         */
        if(top == userNumberLimit)
            return 1;
        if(username.length() < usernameLengthLimitMin)
            return 11;
        if(username.length() > usernameLengthLimit)
            return 12;
        if(!User.isUsernameValid(username))
            return 13;
        if(password.length() < passwordLengthLimitMin)
            return 21;
        if(password.length() > passwordLengthLimit)
            return 22;
        if(!User.isPasswordValid(password))
            return 23;
        if(namepool.find(username) > 0)
            return 14;
        register(username, password);
        return 0;
    }

    public int loginAttempt(String username, String password)
    {
        /* a user tries to log in
         * return 0 stands for login successfully
         * return 1 stands for username invalid
         * return 2 stands for username does not exist
         * return 3 stands for password incorrect
         */
        if(username.length() < usernameLengthLimitMin)
            return 1;
        if(username.length() > usernameLengthLimit)
            return 1;
        if(!User.isUsernameValid(username))
            return 1;
        int userID = namepool.find(username);
        if(userID == 0)
            return 2;
        if(!userList[userID].password.equals(password))
            return 3;
        return 0;
    }
}
