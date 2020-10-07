import java.net.Socket;
import java.net.SocketException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TCPEchoClient {
    public static void main(String[] args) throws IOException{
        if ((args.length != 2 && args.length != 3))
            throw new IllegalArgumentException("Parameter(s) <Server> <Word> [<Port>]");
        String server = args[0];
        byte[] data = args[1].getBytes(); //Convert the echo string into bytes
        int serverPort = (args.length==3) ? Integer.parseInt(args[2]) : 7; //Determine echo port, if none specified set to default 7

        //create socket that is connected to server on required port
        Socket socket = new Socket(server, serverPort); //Creates new socket and connects it to specified server
        System.out.println("Connected to server...sending echo string");

        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();
        out.write(data); //Write data to the outputstream

        int totalBytes = 0;
        int bytesReceived;

        while (totalBytes < data.length){ // While loop to read all the bytes
            bytesReceived = in.read(data, totalBytes, data.length-totalBytes);
            if ( bytesReceived == -1)
                throw new SocketException("Connection closed prematurely");
            totalBytes += bytesReceived;
        }

        System.out.println("Received " + new String(data));
        socket.close();
    }
}
