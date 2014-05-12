import anorm.{NotAssigned, Id, Pk}
import play.api.libs.json._

package object models {
  private[models] implicit val idWriter = new Writes[Pk[Long]] {
    override def writes(pk: Pk[Long]) = pk match {
      case Id(id) => JsNumber(id)
      case NotAssigned => JsNull
    }
  }
}
