
import java.net.ServerSocket

object SimpleServer extends App {
  val serverSocket = new ServerSocket(4998)
  println("Accepting sockets")
  val socket = serverSocket.accept()
  println("Socket accepted: " + socket)

}
