package com.fitlog.bodyweight.models

import org.joda.time.DateTime

sealed case class BodyWeightLogEntryInput(
   weight: Double,
   dateTime: DateTime,
   comment: Option[String],
   bodyFat: Option[Double],
   tags: Option[List[String]])
