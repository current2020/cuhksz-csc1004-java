import java.sql.*;

public class UserList
{
    private static final String DBURL = "jdbc:sqlite:./data/USER_INFO.db";
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
        loadFromDB();
    }

    private void loadFromDB()
    {
        Connection connection;
        Statement statement;
        ResultSet resultSet;
        String sql;
        try
        {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DBURL);
            statement = connection.createStatement();

            sql = "CREATE TABLE IF NOT EXISTS USERS (" + 
                  "USERNAME TEXT PRIMARY KEY NOT NULL, " +
                  "PASSWORD TEXT             NOT NULL) ";
            statement.executeUpdate(sql);

            sql = "SELECT * FROM USERS;";
            resultSet = statement.executeQuery(sql);
            while(resultSet.next())
            {
                String username = resultSet.getString("USERNAME");
                String password = resultSet.getString("PASSWORD");
                namepool.insert(username, new User(username, password));
            }
            resultSet.close();

            statement.close();
            connection.close();

            System.out.println("user_info loaded successfully");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void register(String username, String password)
    {
        namepool.insert(username, new User(username, password));
        SaveIntoDB(username, password);
    }

    private void SaveIntoDB(String username, String password)
    {
        Connection connection;
        Statement statement;
        String sql;
        try
        {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DBURL);
            statement = connection.createStatement();

            sql = String.format("INSERT INTO USERS (USERNAME, PASSWORD) VALUES ('%s', '%s');", username, password);
            statement.executeUpdate(sql);

            statement.close();
            connection.close();

            System.out.println("newly registration stored successfully");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
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
