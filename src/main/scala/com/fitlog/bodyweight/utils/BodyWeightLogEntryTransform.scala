package com.fitlog.bodyweight.utils

import com.fitlog.bodyweight.models.{BodyWeightLogEntryInput, BodyWeightLogEntryOutput}
import org.joda.time.DateTime

trait BodyWeightLogEntryTransform {
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
