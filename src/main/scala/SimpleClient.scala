import java.net.Socket
import java.io.PrintStream
import java.io.InputStreamReader
import java.io.BufferedReader

object SimpleClient extends App {
  println("Socket being created")
  val socket = new Socket("localhost", 4998)
  val input = socket.getInputStream
  val output = new PrintStream(socket.getOutputStream)


}
