package com.fitlog.bodyweight

import java.util.UUID

import org.joda.time.DateTime

import scala.collection.mutable
import scala.util.{Failure, Random, Success, Try}


object Database extends BodyWeightLogEntryTransformer {
  val _log = mutable.Map[String, BodyWeightLogEntryOutput]()

  def save(entry: Try[BodyWeightLogEntryInput]): Try[BodyWeightLogEntryOutput] = entry.flatMap(e => {
    val id = UUID.randomUUID().toString
    val newEntry = transform(e, id, edited = false, DateTime.now())
    _log.get(id) match {
      case None =>
        if (Random.nextInt(100) > 70) {
          Failure(new Exception("random"))
        } else {
          _log.update(id, newEntry)
          Success(newEntry)
        }
      case Some(x) =>
        Failure(new Exception(s"duplicate id '$id' was generated. Use a better PRNG!!!"))
    }
  })


  def put(entryId: Try[String], newEntry: Try[BodyWeightLogEntryInput]): Try[BodyWeightLogEntryOutput] =
    entryId.flatMap(id => {
    newEntry.flatMap(entry => {
      _log.get(id) match {
        case None =>
          Failure(new Exception(s"could not find entry with id=$id"))
        case Some(e) => {
          val updatedEntry = transform(entry, e._id, edited = true, e._serverDateTime)
          _log.update(id, updatedEntry)
          Success(updatedEntry)
        }
      }
    })
  })

  def getLog: Try[List[BodyWeightLogEntryOutput]] = Success(_log.toList.map(_._2))
}
