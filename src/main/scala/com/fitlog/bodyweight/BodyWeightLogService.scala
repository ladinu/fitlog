package com.fitlog.bodyweight

import com.fitlog.bodyweight.commands.{PutEntry, PostEntry, GetLog}
import com.webtrends.harness.service.Service

class BodyWeightLogService extends Service {
  override def serviceName = "BodyWeightLogService"

  override def addCommands = {
    addCommand(GetLog.CommandName, classOf[GetLog])
    addCommand(PostEntry.CommandName, classOf[PostEntry])
    addCommand(PutEntry.CommandName, classOf[PutEntry])
  }
}
