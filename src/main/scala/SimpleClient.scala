import java.net.Socket
import java.io.PrintStream
import java.io.InputStreamReader
import java.io.BufferedReader
import scala.io.StdIn
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object SimpleClient extends App {
  println("Socket being created")
  val socket = new Socket("localhost", 4998)
  val input = socket.getInputStream
  val output = new PrintStream(socket.getOutputStream)
  Future {
    while (true) {
      val currentInput = StdIn.readLine
      output.println(currentInput)
      val p = input.read()
      println(p)
    }
  }

  var tempInput = ""

  while(tempInput!=":quit"){
    val input = StdIn.readLine
    output.println(input)
  }
  socket.close()


}
