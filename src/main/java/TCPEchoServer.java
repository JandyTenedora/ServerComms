import java.net.*;
import java.io.*;

public class TCPEchoServer {
    private static final int BUFFERSIZE = 32;

    public static void main(String[] args) throws IOException {
        if (args.length != 1)
            throw new IllegalArgumentException("Parameter(s): <Port");

        int serverPort = Integer.parseInt(args[0]);

        ServerSocket serverSocket = new ServerSocket(serverPort);

        int receivedMessageSize;
        byte[] receiveBuffer = new byte[BUFFERSIZE];

        while(true) {
            Socket clientSocket = serverSocket.accept();
            SocketAddress clientAddress = clientSocket.getRemoteSocketAddress();
            System.out.println("Client at " + clientAddress);

            InputStream in = clientSocket.getInputStream();
            OutputStream out = clientSocket.getOutputStream();

            while ((receivedMessageSize = in.read(receiveBuffer)) != -1) {
                out.write(receiveBuffer, 0, receivedMessageSize);
            }
            clientSocket.close();
        }
    }
}
