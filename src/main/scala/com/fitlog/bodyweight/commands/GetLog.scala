package com.fitlog.bodyweight.commands

import com.fitlog.bodyweight.{BodyWeightLogEntryOutput, BodyWeightLogEntryTransformer, Database}
import com.webtrends.harness.command.{Command, CommandBean, CommandException, CommandResponse}
import com.webtrends.harness.component.spray.route.SprayGet

import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success, Try}

class GetLog extends BodyWeightLogEntryTransformer with Command with SprayGet {
  implicit val executionContext = context.dispatcher

  override def commandName = GetLog.CommandName
  override def path: String = "/log"
  override def idFormat(id: String) = s"/log/$id"

  def getLog: Try[List[BodyWeightLogEntryOutput]] = Database.getLog match {
    case Failure(ex) =>
      log.error("unable to get log", ex.getStackTrace.mkString("\n\t"))
      Failure(CommandException(GetLog.CommandName, "unable to get log"))
    case l => l
  }

  def execute[T](bean: Option[CommandBean]): Future[CommandResponse[T]] = {
    val p = Promise[CommandResponse[T]]
    Database.getLog match {
      case Failure(ex) =>
        p failure new CommandException(GetLog.CommandName, ex.getMessage, Some(ex))
      case Success(log: List[BodyWeightLogEntryOutput]) =>
        p success CommandResponse[T](Some(log.map(l => transform(l)).asInstanceOf[T]), "json")
    }
    p.future
  }

}

object GetLog {
  def CommandName = "GetLog"
}
