package kpritam.nifty

import java.nio.file.{Files, Path, Paths}
import java.time.Period
import java.util.{Calendar, Date}

import scala.io
import java.io.File
import java.text.SimpleDateFormat

import java.time.Instant
import scala.util.control.NonFatal
import scala.compat.java8.StreamConverters.StreamHasToScala
import java.util.concurrent.TimeUnit

case class FileInfo(path: Path, date: Date):
  private val calendar = Calendar.getInstance()
  def isFriday = dayOfWeek == Calendar.FRIDAY
  def isThursday = dayOfWeek == Calendar.THURSDAY

  def dayOfWeek = 
    calendar.setTime(date)
    calendar.get(Calendar.DAY_OF_WEEK)
  
  def dayOfMonth = 
    calendar.setTime(date)
    calendar.get(Calendar.DAY_OF_MONTH)
    
object FileInfo:
  def from(path: Path): FileInfo =
    FileInfo(
      path,
      format.parse(path.toString.split(".csv").head.split("_").last)
    )
  
def format = new SimpleDateFormat("ddmmyyyy")
def isCsv(path: Path) = path.toString.endsWith(".csv")

def listAllFiles(folder: String) = Files.walk(Paths.get(folder)).toScala[List]

def getAllCsvFileInfo(folder: String): List[FileInfo] =
  listAllFiles(folder)
    .filter(isCsv)
    .map(FileInfo.from)
    .sortBy(_.date)

def friday(fileInfo: FileInfo) = (fileInfo.dayOfWeek + fileInfo.dayOfMonth + 6) / 7

def grpByWeekStartingFromFriday(files: List[FileInfo]) = files.groupBy(friday).view.toList.sortBy(_._1)

def listAllFilesGroupedByWeekStartingFromFriday = getAllCsvFileInfo andThen grpByWeekStartingFromFriday

def startAndEndDayOfWeek(weeks: List[(Int, List[FileInfo])]) = weeks.map {
  case (i, value) => (i, value.headOption.toList ++ value.tail.lastOption.toList)
}