package services

import model.{Catalogue, Song}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.Helpers._
import reactivemongo.api.commands.WriteResult
import repository.CatalogueRepo

import scala.concurrent.{ExecutionContext, Future}

class CatalogueServiceSpec extends PlaySpec with MockitoSugar with GuiceOneAppPerSuite {

  implicit val ec = ExecutionContext.global

  val mockWriteResult = mock[WriteResult]
  private val mockCatalogueRepo = mock[CatalogueRepo]

  private val injector = new GuiceApplicationBuilder()
    .overrides(bind[CatalogueRepo].to(mockCatalogueRepo))
    .injector()

  val mockCatalogueService = injector.instanceOf[CatalogueService]

  val fakesong = Song(1, "bla", "bla", "bla", "////", 1995)

  val fakeCatalogue = Catalogue(List(fakesong))

  "returnAllSongs" should {
    "return all the songs within the catalogue" in {
      when(mockCatalogueRepo.returnCatalogue()).thenReturn(Future.successful(Seq(fakeCatalogue)))
      await(mockCatalogueService.returnAllSongs()) mustBe fakeCatalogue.songs
    }
  }

  "getSongFromCatalogue" should {
    "return one specific song from catalogue" in {
      when(mockCatalogueRepo.getSongFromCatalogue(fakesong.id)).thenReturn(Future(Some(fakesong)))
      await(mockCatalogueService.getSongFromCatalogue(fakesong.id)) mustBe Some(fakesong)

    }
  }


}
