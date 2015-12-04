package com.fitlog.bodyweight.database

import java.util.UUID

import com.fitlog.bodyweight.models.{BodyWeightLogEntryInput, BodyWeightLogEntryOutput}
import com.fitlog.bodyweight.utils.BodyWeightLogEntryTransform
import org.joda.time.DateTime

import scala.collection.mutable
import scala.util.{Failure, Success, Try}


object Database extends BodyWeightLogEntryTransform {
  val log = mutable.Map[String, BodyWeightLogEntryOutput]()

  def save(entry: Try[BodyWeightLogEntryInput]): Try[BodyWeightLogEntryOutput] = entry.flatMap(e => {
    val id = UUID.randomUUID().toString
    val newEntry = transform(e, id, edited = false, DateTime.now())
    log.get(id) match {
      case None =>
        log.update(id, newEntry)
        Success(newEntry)
      case Some(x) =>
        Failure(new Exception(s"duplicate id '$id' was generated. Use a better PRNG!!!"))
    }
  })


  def put(entryId: Try[String], newEntry: Try[BodyWeightLogEntryInput]): Try[BodyWeightLogEntryOutput] =
    entryId.flatMap(id => {
    newEntry.flatMap(entry => {
      log.get(id) match {
        case None =>
          Failure(new Exception(s"could not find entry with id=$id"))
        case Some(e) => {
          val updatedEntry = transform(entry, e._id, edited = true, e._serverDateTime)
          log.update(id, updatedEntry)
          Success(updatedEntry)
        }
      }
    })
  })

  def getLog: Try[List[BodyWeightLogEntryOutput]] = Success(log.toList.map(_._2))
}
