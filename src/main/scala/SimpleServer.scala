
import java.io.InputStreamReader
import java.net.ServerSocket
import java.io.BufferedReader
import java.net.Socket
import java.io.PrintStream
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import java.util.concurrent.ConcurrentHashMap
import scala.collection.JavaConverters._

//Needs loop, accepts new user
object SimpleServer extends App {
  def checkConnections(): Unit = {
    val serverSocket = new ServerSocket(4998)
    println("Accepting sockets")
    val socket = serverSocket.accept()
    println("Socket accepted: " + socket)
    val in = new BufferedReader(new InputStreamReader(socket.getInputStream))
    val out = new PrintStream(socket.getOutputStream)
    Future {
      out.print("Enter your name\n")
      val name = in.readLine()
      val user = User(name, socket, in, out)
      //TODO implement case where it waits until new name is inputted
      users += name -> user

    }
  }

  def nonBlockingRead(in: BufferedReader): Option[String] = {
    if(in.ready()) Some(in.readLine()) else None
  }

  def chat(user: User): Unit = {
    nonBlockingRead(user.in).foreach {
      input => if(input == ":quit"){
        user.sock.close()
        users -= user.name
      } else {
        for((name,u) <- users) {
          u.out.println(user.name+" : "+input)
        }
      }
    }

  }
  case class User(name: String, sock: Socket, in: BufferedReader, out: PrintStream)
  val users = new ConcurrentHashMap[String, User]().asScala



  Future { checkConnections()}
  while(true){
    //TODO - run through users checking input
    for ((name,user) <- users){
      chat(user)

    }
    Thread.sleep(10)
  }




}
