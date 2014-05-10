import controllers.routes
import org.junit.runner.RunWith
import org.specs2.mutable._
import org.specs2.runner._

import models.Movie

import play.api.test._
import play.api.test.Helpers._

@RunWith(classOf[JUnitRunner])
class MoviesSpec extends Specification {
  "Movies controller" should {
    "show a table with all movies" in running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val list = route(FakeRequest(routes.Movies.list)).get

      status(list) must be equalTo OK
      contentAsString(list) must contain("<th>Title</th>")
      contentAsString(list) must contain("<th>Year</th>")
    }

    "save newly created movies" in running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val create = route(FakeRequest(routes.Movies.create)
        .withFormUrlEncodedBody("title" -> "The Matrix", "year" -> "1999")).get

      status(create) must be equalTo SEE_OTHER

      val list = route(FakeRequest(GET, redirectLocation(create).get)).get

      contentAsString(list) must contain("The Matrix")
      contentAsString(list) must contain("1999")
    }

    "be able to edit a movie" in running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val movie = Movie.create(Movie("Titanic", 1995))

      val edit = route(FakeRequest(routes.Movies.update(movie.id.get))
        .withFormUrlEncodedBody("title" -> "The Matrix", "year" -> "1999")).get

      status(edit) must be equalTo SEE_OTHER

      val list = route(FakeRequest(GET, redirectLocation(edit).get)).get

      contentAsString(list) must not contain "Titanic"
      contentAsString(list) must not contain "1995"
    }
  }
}
