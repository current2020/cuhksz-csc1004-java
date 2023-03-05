import java.net.Socket;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class Client
{
    private Socket socket;
    private BufferedReader input;
    private BufferedWriter output;

    public void connect(String serverName, int port) throws IOException
    {
        this.socket = new Socket(serverName, port);
        try
        {
            this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), StandardCharsets.UTF_8));
            this.output = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream(), StandardCharsets.UTF_8));
        }
        catch(IOException e)
        {
            this.socket.close();
            throw e;
        }
    }

    public void say(String text) throws IOException //for test
    {
        output.write("SAY");
        output.newLine();
        output.write(text);
        output.newLine();
        output.flush();
    }

    public String receive() throws IOException //for test
    {
        String command, text, res = "";
        command = input.readLine();
        if(command.equals("QUIT"))
        {
            return "Server is quiting...";
        }
        else if(command.equals("REPLY"))
        {
            text = input.readLine();
            res = "server reply: " + text;
        }
        else
        {
            res = "Unknown Server Reply";
        }
        return res;
    }
}
