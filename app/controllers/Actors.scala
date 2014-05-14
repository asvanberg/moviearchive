package controllers

import models.Actor
import play.api.data.Form
import play.api.data.Forms._
import anorm.{Pk, NotAssigned}
import play.api.mvc._
import play.api.libs.json.Json.toJson

object Actors extends Controller {
  val form: Form[Actor] = Form(mapping(
    "id" -> ignored(NotAssigned: Pk[Long]),
    "firstName" -> nonEmptyText,
    "lastName" -> nonEmptyText
  )(Actor.apply)(Actor.unapply))

  def list = Action { implicit request =>
    render {
      case Accepts.Json() => Ok(toJson(Actor.list))
      case Accepts.Html() => Ok(views.html.actors.index())
    }
  }

  def create = Action {
    Actor.create(Actor("Arnold", "Schwarzenegger"))
    Redirect(routes.Actors.list)
  }
}
