package models

import org.specs2.mutable.Specification
import play.api.test.WithApplication
import anorm.Id
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ActorSpec extends Specification {
  "Actor" should {
    "assign an id to new actors" in new WithApplication {
      val created = Actor.create(Actor("Arnold", "Schwarzenegger"))

      created.id must beLike { case Id(_) => ok }
    }

    "return the correct actor when loading" in new WithApplication {
      val created = Actor.create(Actor("Arnold", "Schwarzenegger"))

      Actor.load(created.id.get) must beSome(created)
    }

    "be able to delete actors" in new WithApplication {
      val created = Actor.create(Actor("Arnold", "Schwarzenegger"))

      Actor.delete(created)

      Actor.load(created.id.get) must beNone
    }

    "include all actors when listing" in new WithApplication {
      val arnold = Actor.create(Actor("Arnold", "Schwarzenegger"))
      val sylvester = Actor.create(Actor("Sylvester", "Stallone"))

      val actors = Actor.list

      actors must contain(arnold)
      actors must contain(sylvester)
    }

    "be able to update an actor" in new WithApplication {
      val arnold = Actor.create(Actor("Arnold", "Swarzneggr"))

      Actor.update(arnold.id.get, Actor("Arnold", "Schwarzenegger"))

      Actor.load(arnold.id.get) must beLike {
        case Some(actor: Actor) => actor.lastName must be equalTo "Schwarzenegger"
      }
    }
  }
}