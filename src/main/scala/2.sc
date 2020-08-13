import java.nio.file.{Files, Path, Paths}
import java.text.SimpleDateFormat
import java.time.Period
import java.util.{Calendar, Date}

import scala.compat.java8.StreamConverters.StreamHasToScala
import java.util.concurrent.TimeUnit

val format = new SimpleDateFormat("ddmmyyyy")

case class FileInfo(path: Path, date: Date) {
  private val calendar = Calendar.getInstance()

  def isFriday = dayOfWeek == Calendar.FRIDAY
  def isThursday = dayOfWeek == Calendar.THURSDAY

  def dayOfWeek = {
    calendar.setTime(date)
    calendar.get(Calendar.DAY_OF_WEEK)
  }

  def dayOfMonth = {
    calendar.setTime(date)
    calendar.get(Calendar.DAY_OF_MONTH)
  }

  def weekOfMonth = {
    calendar.setTime(date)
    calendar.get(Calendar.WEEK_OF_MONTH)
  }

}

object FileInfo {
  val empty = FileInfo(null, null)

  def from(path: Path) =
    FileInfo(
      path,
      format.parse(path.toString.split(".csv").head.split("_").last)
    )
}

def isCsv(path: Path) = path.toString.endsWith(".csv")
def listAllFiles(folder: String) = Files.walk(Paths.get(folder)).toScala[List]

val files =
  listAllFiles("/Users/pritamkadam/Downloads/APR-2020")
    .filter(isCsv)
    .map(FileInfo.from)
    .sortBy(_.date)

def friday(fileInfo: FileInfo) =
  (fileInfo.dayOfWeek + fileInfo.dayOfMonth + 6) / 7

val weeks = files.groupBy(friday).view.toList.sortBy(_._1)

val startAndEndDayOfWeek = weeks.map {
  case (i, value) => (i, value.headOption.toList ++ value.tail.lastOption.toList)
}

println("=" * 80)
weeks.foreach {
  case (i, value) =>
    println(s"========== WEEK-${i} ============")
    println(value.mkString("\n"))
}

println("=" * 80)
startAndEndDayOfWeek.foreach {
  case (i, value) =>
    println(s"========== WEEK-${i} ============")
    println(value.mkString("\n"))
}

println("=" * 80)
