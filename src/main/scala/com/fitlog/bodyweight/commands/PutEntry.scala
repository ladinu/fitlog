package com.fitlog.bodyweight.commands

import com.fitlog.bodyweight._
import com.webtrends.harness.command.{Command, CommandBean, CommandException, CommandResponse}
import com.webtrends.harness.component.spray.route.SprayPut
import spray.http.StatusCodes

import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success, Try}

class PutEntry extends BodyWeightLogEntryTransformer with Command with SprayPut{
  implicit val executionContext = context.dispatcher
  override def responseStatusCode: StatusCodes.Success = StatusCodes.Created
  override def commandName = PostEntry.CommandName
  override def path: String = "/log/$entryId"
  override def idFormat(id: String): String = s"/log/$id"

  def getEntryIdFromBean(bean: Option[CommandBean]): Try[String] = {
    val errMsg: String = "could not find resource"
    bean match {
      case None =>
        log.error("bean not found")
        Failure(new Exception(errMsg))
      case Some(b) =>
        try {
          Success(b("entryId").asInstanceOf[String])
        } catch {
          case ex: ClassCastException =>
            log.error("unable to extract entryId from bean")
            Failure(new Exception(errMsg))
        }
    }
  }


  def execute[T](bean: Option[CommandBean]): Future[CommandResponse[T]] = {
    val p = Promise[CommandResponse[T]]
    Database.put(getEntryIdFromBean(bean), extractLogEntry(bean)) match {
      case Failure(ex) =>
        p failure new CommandException(PutEntry.CommandName, ex.getMessage, Some(ex))
      case Success(entry: BodyWeightLogEntryOutput) =>
        p success CommandResponse[T](Some(transform(entry).asInstanceOf[T]), "json")
    }
    p.future
  }

}

object PutEntry {
  def CommandName = "PutEntry"
}

