import java.sql.*;
import java.util.Date;

public class ChatHistory 
{
    private Connection connection;
    private int idCounter;

    public ChatHistory()
    {
        Statement statement;
        ResultSet resultSet;
        String sql;
        try
        {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:CHAT_INFO.db");
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

    public void close()
    {
        Statement statement;
        String sql;
        try
        {
            statement = connection.createStatement();
            sql = "UPDATE STATS SET IDCOUNTER = %d WHERE ID = 0;";
            sql = String.format(sql, idCounter);
            statement.executeUpdate(sql);
            statement.close();
            connection.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public int newMessage(String senderName, String text, int quotation)
    {
        ++idCounter;
        Statement statement;
        String sql;
        try
        {
            statement = connection.createStatement();
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

    private static String toSafeText(String string)
    {
        return string.replace("'", "''");
    }
}
