import java.sql.*;
import java.util.Date;

public class ChatHistory 
{
    private static final String DBURL = "jdbc:sqlite:./data/CHAT_INFO.db";
    private Connection connection;
    private int idCounter;

    /** connect with CHAT_INFO.db and restore idCounter */
    public ChatHistory()
    {
        Statement statement;
        ResultSet resultSet;
        String sql;
        try
        {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DBURL);
            statement = connection.createStatement();

            sql = "CREATE TABLE IF NOT EXISTS STATS (" + 
                  "ID INT PRIMARY KEY NOT NULL, " + 
                  "IDCOUNTER      INT NOT NULL) " ;
            statement.executeUpdate(sql);

            sql = "CREATE TABLE IF NOT EXISTS CHATS (" +
                  "ID  INT PRIMARY KEY NOT NULL, " +
                  "SENDERNAME     TEXT NOT NULL, " +
                  "TEXT           TEXT NOT NULL, " +
                  "QUOTATION       INT NOT NULL, " +
                  "ISRETREATED     INT NOT NULL, " +
                  "ISEDITED        INT NOT NULL, " +
                  "SENDINGTIME INTEGER NOT NULL) " ;
            statement.executeUpdate(sql);

            sql = "SELECT * FROM STATS;";
            resultSet = statement.executeQuery(sql);
            if(resultSet.next())
            {
                idCounter = resultSet.getInt("IDCOUNTER");
            }
            else
            {
                sql = "INSERT INTO STATS (ID, IDCOUNTER) VALUES (0, 0);";
                statement.executeUpdate(sql);
                idCounter = 0;
            }
            resultSet.close();

            statement.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /** close the connection */
    public void close()
    {
        try
        {
            connection.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * store a new message into database
     * @param senderName
     * @param text
     * @param quotation
     * @return the ID of the new message
     */
    public int newMessage(String senderName, String text, int quotation)
    {
        ++idCounter;
        Statement statement;
        String sql;
        try
        {
            statement = connection.createStatement();
            
            sql = "UPDATE STATS SET IDCOUNTER = %d WHERE ID = 0;";
            sql = String.format(sql, idCounter);
            statement.executeUpdate(sql);

            sql = "INSERT INTO CHATS (ID, SENDERNAME, TEXT, QUOTATION, ISRETREATED, ISEDITED, SENDINGTIME) " +
                  "VALUES (%d, '%s', '%s', %d, %d, %d, %d);";
            sql = String.format(sql, idCounter, senderName, toSafeText(text), quotation, 0, 0, (new Date()).getTime());
            statement.executeUpdate(sql);

            statement.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return idCounter;
    }

    /**
     * edit a message which is already in database
     * @param id
     * @param text
     */
    public void editMessage(int id, String text)
    {
        Statement statement;
        String sql;
        try
        {
            statement = connection.createStatement();
            sql = "UPDATE CHATS SET TEXT = '%s', ISEDITED = 1 WHERE ID = %d";
            sql = String.format(sql, toSafeText(text), id);
            statement.executeUpdate(sql);
            statement.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * retreat a message which is already in database
     * @param id
     */
    public void retreatMessage(int id)
    {
        Statement statement;
        String sql;
        try
        {
            statement = connection.createStatement();
            sql = "UPDATE CHATS SET TEXT = '[Retreated]', ISRETREATED = 1 WHERE ID = %d";
            sql = String.format(sql, id);
            statement.executeUpdate(sql);
            statement.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * restore all chat history and send them to the given threadServer
     * @param threadServer
     */
    public void restore(ThreadServer threadServer)
    {
        Statement statement;
        String sql;
        ResultSet resultSet;
        threadServer.send("~BEGIN-CHATS");
        try
        {
            statement = connection.createStatement();
            sql = "SELECT * FROM CHATS";
            resultSet = statement.executeQuery(sql);
            String idString, senderName, text, quotationString, isRetreatedString, isEditedString, sendingTimeString;
            while(resultSet.next())
            {
                idString = Integer.toString(resultSet.getInt("ID"));
                senderName = resultSet.getString("SENDERNAME");
                text = resultSet.getString("TEXT");
                quotationString = Integer.toString(resultSet.getInt("QUOTATION"));
                isRetreatedString = Integer.toString(resultSet.getInt("ISRETREATED"));
                isEditedString = Integer.toString(resultSet.getInt("ISEDITED"));
                sendingTimeString = Long.toString(resultSet.getLong("SENDINGTIME"));
                threadServer.send("~CHAT", idString, senderName, text, quotationString, isRetreatedString, isEditedString, sendingTimeString);
            }
            resultSet.close();
            statement.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        threadServer.send("~END-CHATS");
    }

    /**
     * make a string safe for SQL
     * @param string
     * @return the safe string
     */
    private static String toSafeText(String string)
    {
        return string.replace("'", "''");
    }
}
