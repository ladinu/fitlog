package com.fitlog.bodyweight.commands

import com.fitlog.bodyweight._
import com.webtrends.harness.command.{Command, CommandBean, CommandException, CommandResponse}
import com.webtrends.harness.component.ComponentHelper
import com.webtrends.harness.component.spray.route.SprayPost
import spray.http.StatusCodes

import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success, Try}

class PostEntry extends BodyWeightLogEntryTransformer with Command with SprayPost with ComponentHelper {
  implicit val executionContext = context.dispatcher

  override def responseStatusCode: StatusCodes.Success = StatusCodes.Created
  override def commandName = PostEntry.CommandName
  override def path: String = "/log"

  override def idFormat(id: String): String = s"$path/$id"

  def execute[T](bean: Option[CommandBean]): Future[CommandResponse[T]] = {
    val p = Promise[CommandResponse[T]]
    Database.save(extractLogEntry(bean)) match {
      case Failure(ex) =>
        p failure new CommandException(PostEntry.CommandName, ex.getMessage, Some(ex))
      case Success(entry: BodyWeightLogEntryOutput) =>
        p success CommandResponse[T](Some(transform(entry).asInstanceOf[T]), "json")
    }
    p.future
  }

}

object PostEntry {
  def CommandName = "PostEntry"
}
