package repository

import model.{Catalogue, Song}
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.test.Helpers._

import scala.concurrent.{ExecutionContext, Future}

class CatalogueRepoSpec extends PlaySpec with MockitoSugar with GuiceOneAppPerSuite {

  implicit val ec = ExecutionContext.global

  val mockCatalogueRepository = app.injector.instanceOf[CatalogueRepo]

  val song = Song(1, "bla", "bla", "bla", "somePath", 1995)
  val fakeSongs = List(song)
  val catalogue = Catalogue(fakeSongs)


  "returnCatalogue" should {
    "return a seq on catalogues" in {
      await(mockCatalogueRepository.removeAll())
      await(mockCatalogueRepository.createCatalogue(catalogue))
      await(mockCatalogueRepository.returnCatalogue()) mustBe List(Catalogue(fakeSongs))
    }
  }

  "getSongFromCatalogue" should {
    "return a single song" in {
      await(mockCatalogueRepository.removeAll())
      await(mockCatalogueRepository.createCatalogue(catalogue))
      await(mockCatalogueRepository.getSongFromCatalogue(fakeSongs.map(_.id).head)) mustBe Some(song)
    }
  }

}
