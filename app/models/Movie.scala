package models

import anorm._
import anorm.SqlParser._
import play.api.db.DB
import play.api.Play.current
import scala.language.postfixOps

case class Movie(id: Pk[Long], title: String, year: Int) {

}

object Movie {
  private val userParser: RowParser[Movie] = long("id") ~ str("title") ~ int("year") map {
    case id ~ title ~ year => Movie(Id(id), title, year)
  }

  def apply(title: String, year: Int): Movie = apply(NotAssigned, title, year)

  def create(movie: Movie): Movie = DB.withConnection { implicit connection =>
    val id = SQL("INSERT INTO movie (title, year) VALUES ({title}, {year})")
      .on('title -> movie.title, 'year -> movie.year)
      .executeInsert(scalar[Long].single)
    Movie(Id(id), movie.title, movie.year)
  }

  def load(id: Long): Option[Movie] = DB.withConnection { implicit connection =>
    SQL("SELECT * FROM movie WHERE id = {id}")
    .on('id -> id)
    .as(userParser.singleOpt)
  }

  def update(id: Long, movie: Movie): Unit = DB.withConnection { implicit connection =>
    SQL("UPDATE movie SET title = {title}, year = {year} WHERE id = {id}")
    .on('title -> movie.title, 'year -> movie.year, 'id -> id)
    .executeUpdate
  }

  def delete(movie: Movie): Unit = delete(movie.id.get)
  def delete(id: Long): Unit = DB.withConnection { implicit connection =>
    SQL("DELETE FROM movie WHERE id = {id}")
    .on('id -> id)
    .execute
  }

  def list: Seq[Movie] = DB.withConnection { implicit connection =>
    SQL("SELECT * FROM movie")
    .as(userParser *)
  }
}