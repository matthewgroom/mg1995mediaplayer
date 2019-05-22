package controllers

import akka.stream.Materializer
import model.{Playlist, Song}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.http.Status
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.{AnyContentAsEmpty, AnyContentAsFormUrlEncoded}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.{CatalogueService, PlaylistService}

import scala.concurrent.{ExecutionContext, Future}


class MainControllerSpec extends PlaySpec with MockitoSugar with GuiceOneAppPerSuite {

  implicit val ec = ExecutionContext.global

  private val mockPlaylistService = mock[PlaylistService]
  private val mockCatalogueService = mock[CatalogueService]
  private implicit lazy val materializer: Materializer = app.materializer
  private implicit lazy val fakeRequest: FakeRequest[AnyContentAsEmpty.type] = FakeRequest()

  private val injector = new GuiceApplicationBuilder()
    .overrides(bind[CatalogueService].to(mockCatalogueService))
    .overrides(bind[PlaylistService].to(mockPlaylistService))
    .injector()

  val controller = injector.instanceOf[MainController]

  val fakeSongs = List(Song(1, "bla", "bla", "bla", "////", 1995))

  val fakeListOfPlaylist = List(Playlist.apply(id = 1, name = "bla", songs = fakeSongs))

  val fakePlaylist = Playlist(10,"testPlaylist",fakeSongs)

  val fakeSong = Song(1, "bla", "bla", "bla", "////", 1995)

  "Index" should {
    "return the index page with a list of playlist or none at all" in {
      when(mockPlaylistService.returnAllPlaylist()).thenReturn(Future.successful(fakeListOfPlaylist))
      val result = call(controller.index(), fakeRequest)
      status(result) mustBe Status.OK
    }
  }

  "ErrorPage" should {
    "errorPage should display when an issue occurs" in {
    val result = call(controller.index(), fakeRequest)
    Future.successful(result)
    }
  }

  "selectPlaylist" should {
    "return 200 happy path selectPlaylist" in {
      when(mockPlaylistService.getPlaylist(1)).thenReturn(Future.successful(Some(fakePlaylist)))
      val result = call(controller.selectPlaylist(1),fakeRequest)
      status(result) mustBe Status.OK
    }
    "return 200 unhappy path selectPlaylist" in {
      when(mockPlaylistService.getPlaylist(1)).thenReturn(Future.successful(None))
      val result = call(controller.selectPlaylist(1),fakeRequest)
      status(result) mustBe Status.OK
    }
  }

  "createPlaylistPage" should {
    "return 200 when the PlaylistPage is successfully displayed" in {
      when(mockCatalogueService.returnAllSongs()).thenReturn(Future.successful(fakeSongs))
      val result = call(controller.createPlaylistPage(), fakeRequest)
      status(result) mustBe Status.OK
    }
  }

  "createEmptyPlaylist" should {
    val fakeRequest = FakeRequest(routes.MainController.createEmptyPlaylist())
    val requestWithFormData: FakeRequest[AnyContentAsFormUrlEncoded] = fakeRequest.withFormUrlEncodedBody(
      "name" -> "name"
    )

    "return 200 when a empty playlist is created" in {
      when(mockPlaylistService.createPlaylist(fakePlaylist)).thenReturn(Future.successful(true))
      when(mockCatalogueService.returnAllSongs()).thenReturn(Future.successful(fakeSongs))
      val result = await(call(controller.createPlaylistPage(), requestWithFormData))
      result.header.status mustBe Status.OK
    }
  }

  "insertSongToPlaylist" should {
    "return 200 when a song is inserted into a playlist" in {
      when(mockPlaylistService.insertSongToPlaylist(1, 1)).thenReturn(Future(Some(fakePlaylist)))
      when(mockCatalogueService.returnAllSongs()).thenReturn(Future(fakeSongs))
      val result = call(controller.insertSongToPlaylist(1,1), fakeRequest)
      status(result) mustBe Status.OK
    }
  }

  "listViaAlbum" should {
    "return 200 if playlist is found" in {
      when(mockPlaylistService.getPlaylist(1)).thenReturn(Future(Some(fakePlaylist)))
      val result = call(controller.listViaAlbum(1), fakeRequest)
      status(result) mustBe Status.OK
    }

    "return 200 if a playlist isn't found" in {
      when(mockPlaylistService.getPlaylist(1)).thenReturn(Future(None))
      val result = call(controller.listViaAlbum(1), fakeRequest)
      status(result) mustBe Status.OK
    }
  }

  "listViaSong" should {
    "return 200 if playlist is found" in {
      when(mockPlaylistService.getPlaylist(1)).thenReturn(Future(Some(fakePlaylist)))
      val result = call(controller.listViaSong(1), fakeRequest)
      status(result) mustBe Status.OK
    }

    "return 200 if playlist isn't found" in {
      when(mockPlaylistService.getPlaylist(1)).thenReturn(Future(None))
      val result = call(controller.listViaSong(1), fakeRequest)
      status(result) mustBe Status.OK
    }
  }

  "listViaRandom" should {
    "return 200 if playlist is found" in {
      when(mockPlaylistService.getPlaylist(1)).thenReturn(Future(Some(fakePlaylist)))
      val result = call(controller.listViaRandom(1), fakeRequest)
      status(result) mustBe Status.OK
    }

    "return 200 if playlist isn't found" in {
      when(mockPlaylistService.getPlaylist(1)).thenReturn(Future(None))
      val result = call(controller.listViaRandom(1), fakeRequest)
      status(result) mustBe Status.OK
    }
  }

}