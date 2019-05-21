package services

import akka.stream.Materializer
import model.{Catalogue, Song}
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest
import org.mockito.Mockito._
import play.api.test.Helpers._
import reactivemongo.api.commands.WriteResult
import repository.{CatalogueRepo, PlaylistRepo}

import scala.concurrent.{ExecutionContext, Future}

class CatalogueServiceSpec extends PlaySpec with MockitoSugar with GuiceOneAppPerSuite {

  implicit val ec = ExecutionContext.global

  val mockWriteResult = mock[WriteResult]
  private val mockCatalogueRepo = mock[CatalogueRepo]
  private val mockPlaylistService = mock[PlaylistService]
  private implicit lazy val materializer: Materializer = app.materializer
  private implicit lazy val fakeRequest: FakeRequest[AnyContentAsEmpty.type] = FakeRequest()

  private val injector = new GuiceApplicationBuilder()
    .overrides(bind[PlaylistService].to(mockPlaylistService))
    .overrides(bind[CatalogueRepo].to(mockCatalogueRepo))
    .injector()

  val catalogueService = injector.instanceOf[CatalogueService]

  val song = Song(1, "bla", "bla", "bla", "////", 1995)

  val catalogue = Catalogue(List(song))

  "insertCatalogue" should {
    "insert one catalogue" in {
      when(mockCatalogueRepo.createCatalogue(catalogue)).thenReturn(Future(mockWriteResult))
      when(catalogueService.insertCatalogue(catalogue)).thenReturn(Future(mockWriteResult.ok))
    }
  }

  "returnAllSongs" should {
    "return all the songs within the catalogue" in {
      when(mockCatalogueRepo.returnCatalogue()).thenReturn(Future(Seq(catalogue)))
      when(catalogueService.returnAllSongs()).thenReturn(Future(Seq(song)))
    }
  }

  "getSongFromCatalogue" should {
    "return one specific song from catalogue" in {
      when(mockCatalogueRepo.getSongFromCatalogue(1)).thenReturn(Future(Some(song)))
      when(catalogueService.getSongFromCatalogue(1)).thenReturn(Future(Some(song)))
    }
  }


}
