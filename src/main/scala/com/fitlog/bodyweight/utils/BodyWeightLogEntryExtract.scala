package com.fitlog.bodyweight.utils

import com.fitlog.bodyweight.commands.PostEntry
import com.fitlog.bodyweight.models.BodyWeightLogEntryInput
import com.webtrends.harness.command.{CommandBean, CommandException}
import net.liftweb.json._
import net.liftweb.json.ext.JodaTimeSerializers

import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

trait BodyWeightLogEntryExtract {
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
