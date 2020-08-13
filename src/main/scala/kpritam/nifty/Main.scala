package kpritam.nifty

import java.nio.file.{Files, Path, Paths}

import scala.io
import java.io.File
import java.text.SimpleDateFormat

import java.util.Date
import java.time.Instant

import scala.util.control.NonFatal

@main
def main = 
  val files = listAllFilesGroupedByWeekStartingFromFriday("/Users/pritamkadam/Downloads/APR-2020")
  val startAndEnd: List[(Int, List[FileInfo])] = startAndEndDayOfWeek(files)
  val diffs = startAndEnd.map {
    case (i, List(start, end)) =>
      val startClosePrice = closePriceAt("9:30", start)
      val endClosePrice = closePriceAt("14:30", end)
      val diff = endClosePrice.flatMap(e => startClosePrice.map(e - _))
      (i, List(start.path, end.path) ++ diff.toList)
    case (i, _) => (i, List.empty)
  }
  println(diffs.mkString("\n"))

def read(path: Path): List[String] = 
  io.Source.fromFile(new File(path.toString)).getLines.toList.drop(1)

def closePriceAt(at: String, fileInfo: FileInfo) = 
  read(fileInfo.path).map(Row.from).find(_.time.startsWith(at)).map(_.close)
