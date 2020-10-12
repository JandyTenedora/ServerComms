import org.apache.spark.{SparkConf, SparkContext}

case class TempData(day: Int, doy: Int, month: Int, year: Int, precip: Double, snow: Double, tave: Double, tmax: Double, tmin: Double)

object TempData {
  def toDoubleOrNegative(s:String): Double = {
    try {
      s.toDouble
    } catch {
      case _:NumberFormatException => -1
    }
  }
}


object RDDTempData {
  val dataDirectory = "/home/jtenedor/Downloads/BigDataAnalyticswithSpark-master/TX417945_8515.csv"
  val dataDirectory2= "/home/jtenedor/Downloads/BigDataAnalyticswithSpark-master/MN212142_9392.csv"
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Temperature data").setMaster("local[*]") // Running on local machine using all available computing resources
    val sc = new SparkContext(conf)

    val lines = sc.textFile(dataDirectory2).filter(!_.contains("Day"))

    lines.take(5) foreach println

    val data = lines.flatMap {
      line =>
        val p = line.split(",")
        if (p(7)=="." ||p(8) == "." || p(9) ==".") Seq.empty else
          Seq(TempData(p(0).toInt, p(1).toInt, p(2).toInt, p(4).toInt, TempData.toDoubleOrNegative(p(5)), TempData.toDoubleOrNegative(p(6)), p(7).toDouble,
            p(8).toDouble, p(9).toDouble))
    }

    println(data.max()(Ordering.by(_.tmax)))

    println(data.reduce((td1,td2) => if (td1.tmax >= td2.tmax) td1 else td2))


  }

}
