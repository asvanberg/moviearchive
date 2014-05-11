package models

import org.junit.runner.RunWith
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test._
import anorm.Id

@RunWith(classOf[JUnitRunner])
class MovieSpec extends Specification {
  "Movie" should {
    "assign an id when saving a movie" in new WithApplication {
      val created = Movie.create(Movie("Titanic", 1997))

      created.id must beLike { case Id(_) => ok }
    }

    "return the correct movie when loading" in new WithApplication {
      val created = Movie.create(Movie("Titanic", 1997))
      val loaded = Movie.load(created.id.get)

      loaded must beSome(created)
    }

    "be able to delete movies" in new WithApplication {
      val created = Movie.create(Movie("Titanic", 1997))

      Movie.delete(created)

      Movie.load(created.id.get) must beNone
    }

    "list all movies" in new WithApplication {
      val titanic = Movie.create(Movie("Titanic", 1997))
      val matrix = Movie.create(Movie("The Matrix", 1999))

      val movies = Movie.list

      movies should contain(titanic)
      movies should contain(matrix)
    }
  }
}
