import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkContext
import java.sql.Timestamp
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

//mvn scala:run -DmainClass=batchAnalytics

case class WebLog(host: String,
                  timestamp: Timestamp,
                  request: String,
                  http_reply: Int,
                  bytes: Long
                 )
object batchAnalytics {
  val spark: SparkSession = SparkSession.builder().master("local[1]").appName("batch analytics").getOrCreate()
  import spark.implicits._

  def main(args: Array[String]): Unit = {
    val logsDirectory = "/home/jtenedor/Documents/Spark Streaming/nasa_dataset_july_1995"
    val rawLogs = spark.read.json(logsDirectory)
    val preparedLogs = rawLogs.withColumn("http_reply", col("http_reply").cast(IntegerType))
    val preparedLogsNew = preparedLogs.withColumn("timestamp", col("timestamp").cast(TimestampType))
    val weblogs = preparedLogsNew.as[WebLog]
    println(weblogs.count)
    println("\n\n\n\nRIGHT HEEERRRRREE!!!\n\n\n\n")
  }
}

