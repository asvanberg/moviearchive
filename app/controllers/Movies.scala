package controllers

import models.Movie
import play.api.data.Form
import play.api.data.Forms._
import anorm.{Pk, NotAssigned}
import play.api.mvc._
import play.api.i18n.Messages

object Movies extends Controller {
  val form: Form[Movie] = Form(mapping(
    "id" -> ignored(NotAssigned: Pk[Long]),
    "title" -> nonEmptyText,
    "year" -> number(min = 1900)
  )(Movie.apply)(Movie.unapply))

  def list = Action { implicit request =>
    Ok(views.html.movies.index(Movie.list))
  }

  def add = Action {
    Ok(views.html.movies.add(form))
  }

  def create = Action { implicit request =>
    form.bindFromRequest.fold(
      hasErrors => BadRequest(views.html.movies.add(hasErrors)),
      movie => {
        Movie.create(movie)
        Redirect(routes.Movies.list)
          .flashing("success" -> Messages("added", movie.title, movie.year))
      }
    )
  }

  def edit(id: Long) = Action {
    Movie.load(id).map { movie =>
      val filled: Form[Movie] = form.fill(movie)
      Ok(views.html.movies.edit(id, filled))
    }.getOrElse(NotFound)
  }

  def update(id: Long) = Action { implicit request =>
    Movie.load(id).map { movie =>
      form.bindFromRequest.fold(
        hasErrors => BadRequest(views.html.movies.edit(id, hasErrors)),
        editedMovie => {
          Movie.update(id, editedMovie)
          Redirect(routes.Movies.list)
            .flashing("success" -> Messages("updated", movie.title, movie.year, editedMovie.title, editedMovie.year))
        }
      )
    }.getOrElse(NotFound)
  }

  def delete(id: Long) = Action {
    Movie.load(id).map { movie =>
      Movie.delete(movie)
      Redirect(routes.Movies.list)
        .flashing("success" -> Messages("deleted", movie.title, movie.year))
    }.getOrElse(NotFound)
  }
}