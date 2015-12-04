package com.fitlog.bodyweight.models

import org.joda.time.DateTime

sealed case class BodyWeightLogEntryOutput(
    weight: Double,
    dateTime: DateTime,
    comment: Option[String],
    bodyFat: Option[Double],
    _edited: Boolean,
    _serverDateTime: DateTime,
    _id: String)




