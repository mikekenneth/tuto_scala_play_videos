package controllers

import javax.inject._
import play.api.mvc._
import play.api.i18n._
import models.TaskListInMemoryModel
import play.api.libs.json._
import akka.actor.{Actor, ActorSystem, Props}
import akka.stream.Materializer
import play.api.libs.streams.ActorFlow


import actors.{ChatActor, ChatManager}



@Singleton
class WebSocketChat  @Inject() (cc: ControllerComponents)(implicit system: ActorSystem, mat: Materializer)
  extends AbstractController(cc) {

  val manager = system.actorOf(Props[ChatManager], "Manager")

  def index = Action { implicit request =>
    Ok(views.html.chatPage())

  }

  def socket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef { out =>
      ChatActor.props(out, manager)
    }
  }
}
