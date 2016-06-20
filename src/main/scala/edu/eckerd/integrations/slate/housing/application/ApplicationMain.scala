package edu.eckerd.integrations.slate.housing.application

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import edu.eckerd.integrations.slate.housing.application.actors.SupervisorActor

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.Try

/**
  * Created by davenpcm on 6/17/16.
  */
object ApplicationMain extends App {
  val system = ActorSystem("HRSys")
  val supervisor = system.actorOf(SupervisorActor.props, "super")
  val config = ConfigFactory.load()
  val slateConfig = config.getConfig("slate")
  val user = slateConfig.getString("user")
  val password = slateConfig.getString("password")
  supervisor ! SupervisorActor.Request(
    "https://eck.test.technolutions.net/manage/query/run?id=a8274b1a-9860-4f79-91c2-268c27d6338b&h=7215c6fa-ea25-e33d-0d02-5bd30b28f30e&cmd=service&output=json",
    user,
    password
  )

  Thread.sleep(5000)
  supervisor ! SupervisorActor.TerminateSys

  Await.result(system.whenTerminated, Duration(20, "seconds"))
}
