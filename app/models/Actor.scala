package models

import anorm._
import anorm.SqlParser._
import play.api.db.DB
import play.api.Play.current

case class Actor(id: Pk[Long], firstName: String, lastName: String) {

}

object Actor {
  private val actorParser = long("id") ~ str("firstName") ~ str("lastName") map {
    case id ~ firstName ~ lastName => Actor(Id(id), firstName, lastName)
  }

  def apply(firstName: String, lastName: String): Actor = apply(NotAssigned, firstName, lastName)

  def create(actor: Actor): Actor = DB.withConnection { implicit connection =>
    val id = SQL("INSERT INTO actor (firstName, lastName) VALUES ({firstName}, {lastName})")
      .on('firstName -> actor.firstName, 'lastName -> actor.lastName)
      .executeInsert(scalar[Long].single)
    Actor(Id(id), actor.firstName, actor.lastName)
  }

  def load(id: Long): Option[Actor] = DB.withConnection { implicit connection =>
    SQL("SELECT * FROM actor WHERE id = {id}")
      .on('id -> id)
      .as(actorParser.singleOpt)
  }

  def delete(actor: Actor): Unit = DB.withConnection { implicit connection =>
    SQL("DELETE FROM actor WHERE id = {id}")
      .on('id -> actor.id.get)
      .execute
  }

  def list: Seq[Actor] = DB.withConnection { implicit connection =>
    SQL("SELECT * FROM actor")
      .as(actorParser.*)
  }

  def update(id: Long, actor: Actor): Unit = DB.withConnection { implicit connection =>
    SQL("UPDATE actor SET firstName = {firstName}, lastName = {lastName} WHERE id = {id}")
      .on('firstName -> actor.firstName, 'lastName -> actor.lastName, 'id -> id)
      .executeUpdate
  }
}