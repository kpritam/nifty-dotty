package kpritam.nifty

import java.text.SimpleDateFormat
import java.util.Date

import scala.util.control.NonFatal

//private val dateTimeFormatter = new SimpleDateFormat("yyyyMMddhh")
private val dateTimeFormatter = new SimpleDateFormat("dd/mm/yyyy")

case class Row(
  ticker: String,
  date: Date,
  time: String,
  open: Double,
  high: Double,
  low: Double,
  close: Double,
  volume: Long,
  openInterest: Long
)

object Row:
  def from(str: String): Row =
    val arr = str.split(",").map(_.trim)
    if arr.length == 9
    then Row(
          arr(0),
          date(arr(1)),
          arr(2),
          arr(3).toDouble,
          arr(4).toDouble,
          arr(5).toDouble,
          arr(6).toDouble,
          arr(7).toLong,
          arr(8).toLong
        )
    else throw new RuntimeException("Invalid row: " + str)

def date(date: String) = 
  try dateTimeFormatter.parse(date)
  catch
    case NonFatal(e) => throw new RuntimeException(s"Failed to parse date: $date, reason: ${e.getMessage}")