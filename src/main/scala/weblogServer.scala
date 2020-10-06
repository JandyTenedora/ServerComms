import java.sql.Timestamp

import scala.collection.mutable

class weblogServer {
  val serverPort = 9999
  case class WebLog(host: String,
                    timestamp: Timestamp,
                    request: String,
                    http_reply: Int,
                    bytes: Long
                   )
  val logsDirectory = "/home/jtenedor/Documents/Spark Streaming/nasa_dataset_july_1995"
}

import java.net._
import java.io._
import java.sql.Timestamp
import scala.concurrent.Future
import scala.annotation.tailrec
import scala.collection.JavaConverters._
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import scala.collection.mutable.StringBuilder

import scala.concurrent.ExecutionContext.Implicits.global



class SocketHandler(sparkSession: SparkSession, port: Int, data: Dataset[WebLog]) {
  val connectionWidget = new mutable.StringBuilder()
  val dataWidget = new mutable.StringBuilder()
  val logDelay = 500 // millis
  @volatile var active = false

  // non blocking start of the socket handler
  def start() : Unit = {
    active = true
    new Thread() {
      override def run() {
        connectionWidget.append("Server starting...")
        acceptConnections()
        connectionWidget.append("Server stopped")
      }
    }.start()
  }

  def stop() {
    active = false
  }

  @tailrec
  final def acceptConnections(): Unit = {
    val server: ServerSocket = new ServerSocket(port)
    val socket = server.accept()
    connectionWidget.append("Accepting connection from: " + socket)
    serve(socket)
    if (active) {
      acceptConnections()
    } else {
      () // finish recursing for new connections
    }
  }

  // 1-thread per connection model for example purposes.
  def serve(socket: Socket) = {
    import sparkSession.implicits._
    val minTimestamp  = data.select(min($"timestamp")).as[Timestamp].first
    val now = System.currentTimeMillis
    val offset = now - minTimestamp.getTime()
    val offsetData = data.map(weblog => weblog.copy(timestamp = new Timestamp(weblog.timestamp.getTime+ offset)))
    val jsonData = offsetData.toJSON
    val iter = jsonData.toLocalIterator.asScala
    new Thread() {
      override def run() {
        val out = new PrintStream(socket.getOutputStream())
        connectionWidget.append("Starting data stream for: " + socket.getInetAddress() + "]")
        while(iter.hasNext && active) {
          val data = iter.next()
          out.println(data)
          dataWidget.append(s"[${socket.getInetAddress()}] sending: ${data.take(40)}...")
          out.flush()
          Thread.sleep(logDelay)
        }
        out.close()
        socket.close()
      }
    }.start()
  }
}

