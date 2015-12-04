package com.fitlog.bodyweight.databse

import com.fitlog.bodyweight.commands.PostEntry
import com.webtrends.harness.command.{CommandBean, CommandException}
import net.liftweb.json.ext.JodaTimeSerializers
import net.liftweb.json.{JObject, NoTypeHints, Serialization}
import org.joda.time.DateTime

import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}


sealed case class BodyWeightLogEntryInput(
   weight: Double,
   dateTime: DateTime,
   comment: Option[String],
   bodyFat: Option[Double],
   tags: Option[List[String]])


sealed case class BodyWeightLogEntryOutput(
   weight: Double,
   dateTime: DateTime,
   comment: Option[String],
   bodyFat: Option[Double],
   _edited: Boolean,
   _serverDateTime: DateTime,
   _id: String)

trait BodyWeightLogEntryUtils {
  implicit val formats = Serialization.formats(NoTypeHints) ++ JodaTimeSerializers.all
  def extractLogEntry(bean: Option[CommandBean]): Try[BodyWeightLogEntryInput] = {
    val parseException = CommandException(PostEntry.CommandName, "unable to parse entry")
    bean match {
      case None => Failure(parseException)
      case Some(b) => try {
        Success(b(CommandBean.KeyEntity).asInstanceOf[JObject].extract[BodyWeightLogEntryInput])
      } catch {
        case NonFatal(e) =>
          println(e)
          Failure(parseException)
      }
    }
  }
}

trait BodyWeightLogEntryTransformer extends BodyWeightLogEntryUtils {
  def idFormat(id: String): String = identity(id)
  def dateTimeFormat(dt: DateTime): DateTime = identity(dt)

  def transform(input: BodyWeightLogEntryInput, id: String,
                edited: Boolean,
                dateTime: DateTime): BodyWeightLogEntryOutput = input match {
    case BodyWeightLogEntryInput(_1, _2, _3, _4, _) => BodyWeightLogEntryOutput(_1, _2, _3, _4, edited, dateTime, id)
  }
  def transform(a: BodyWeightLogEntryOutput): BodyWeightLogEntryOutput = a match {
    case BodyWeightLogEntryOutput(_1, _2, _3, _4, _5, _6, _7) => BodyWeightLogEntryOutput(
      _1, dateTimeFormat(_2), _3, _4, _5, _6, idFormat(_7))
  }
}