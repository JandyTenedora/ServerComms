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
  val input = new BufferedReader(new InputStreamReader(socket.getInputStream))
  val output = new PrintStream(socket.getOutputStream)
  Future {
    while (true) {
      val p = input.readLine()
      println(p)
    }
  }



  var tempInput=""
  while(tempInput!=":quit"){
    println("made it to line 25")
    val tempInput = StdIn.readLine
    output.println(tempInput)
  }
  socket.close()


}
