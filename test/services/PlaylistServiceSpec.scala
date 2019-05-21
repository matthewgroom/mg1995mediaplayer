package services

import akka.stream.Materializer
import model.{Playlist, Song}
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest
import reactivemongo.api.commands.WriteResult
import repository.PlaylistRepo

import scala.concurrent.{ExecutionContext, Future}

class PlaylistServiceSpec extends PlaySpec with MockitoSugar with GuiceOneAppPerSuite {

  implicit val ec = ExecutionContext.global

  val mockWriteResult = mock[WriteResult]
  private val mockPlaylistRepo = mock[PlaylistRepo]
  private val mockCatalogueService = mock[CatalogueService]
  private implicit lazy val materializer: Materializer = app.materializer
  private implicit lazy val fakeRequest: FakeRequest[AnyContentAsEmpty.type] = FakeRequest()

  private val injector = new GuiceApplicationBuilder()
    .overrides(bind[CatalogueService].to(mockCatalogueService))
    .overrides(bind[PlaylistRepo].to(mockPlaylistRepo))
    .injector()

  val playlistService = injector.instanceOf[PlaylistService]

  val fakeSongs = List(Song(1, "bla", "bla", "bla", "////", 1995))

  val fakePlaylist = Playlist(10,"testPlaylist",fakeSongs)

  "createPlaylist" should {
    "insert a playlist collection into the database" in {
      when(mockPlaylistRepo.createPlaylist(fakePlaylist)).thenReturn(Future(mockWriteResult))
      when(playlistService.createPlaylist(fakePlaylist)).thenReturn(Future(true))
    }
  }

  "getPlaylist" should {
    "return one playlist" in {

    }
  }


  "findSongWithinPlaylist" should {
    "return one song within playlist" in {

    }
  }

  "returnAllPlaylist" should {
    "return all playlist's" in {

    }
  }

  "insertSongToPlaylist" should {
    "insert one song to playlist" in {

    }
  }

}
