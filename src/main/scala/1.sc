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

  private def dayOfWeek = {
    calendar.setTime(date)
    calendar.get(Calendar.DAY_OF_WEEK)
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

println(files.mkString("\n"))

def getDateDiffInDays(date1: Date, date2: Date) = {
  val diffInMillies = date2.getTime - date1.getTime
  TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)
}

def filterWithFallback(files: List[FileInfo], p: FileInfo => Boolean) =
  files.tail
    .scan(files.head) { (prev, cur) =>
      if (p(cur) || getDateDiffInDays(prev.date, cur.date) >= 7) cur
      else prev
    }
    .distinct

def filterFridayFallbackToNext(files: List[FileInfo]) =
  filterWithFallback(files, _.isFriday)
def filterThursdayFallbackToPrev(files: List[FileInfo]) =
  filterWithFallback(files, _.isThursday)

println("=" * 80)
filterFridayFallbackToNext(files).foreach(println)
println("=" * 80)
filterThursdayFallbackToPrev(files).foreach(println)
