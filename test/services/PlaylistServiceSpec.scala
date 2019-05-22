package services

import model.{Playlist, Song}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import repository.PlaylistRepo
import play.api.test.Helpers._

import scala.concurrent.{ExecutionContext, Future}

class PlaylistServiceSpec extends PlaySpec with MockitoSugar with GuiceOneAppPerSuite {

  implicit val ec = ExecutionContext.global

  private val mockPlaylistRepo = mock[PlaylistRepo]
  private val mockCatalogueService = mock[CatalogueService]

  private val injector = new GuiceApplicationBuilder()
    .overrides(bind[PlaylistRepo].to(mockPlaylistRepo))
    .overrides(bind[CatalogueService].to(mockCatalogueService))
    .injector()

  val mockplaylistService = injector.instanceOf[PlaylistService]

  val fakeSong = Song(1, "bla", "bla", "bla", "////", 1995)
  val fakeListSongs = List(Song(1, "bla", "bla", "bla", "////", 1995))
  val fakePlaylist = Playlist(10,"testPlaylist",fakeListSongs)

  "getPlaylist" should {
    "return one playlist" in {
      when(mockPlaylistRepo.findPlaylist(fakePlaylist.id)).thenReturn(Future.successful(Some(fakePlaylist)))
      await(mockplaylistService.getPlaylist(fakePlaylist.id)) mustBe Some(fakePlaylist)
    }
  }

  "returnAllPlaylist" should {
    "return all playlist's" in {
      when(mockPlaylistRepo.findAllPlaylists()).thenReturn(Future.successful(Seq(fakePlaylist)))
      await(mockplaylistService.returnAllPlaylist()) mustBe Seq(fakePlaylist)
    }
  }

  "insertSongToPlaylist" should {
    "insert one song to playlist" in {
      when(mockCatalogueService.getSongFromCatalogue(fakeSong.id)).thenReturn(Future.successful(Some(fakeSong)))
      when(mockPlaylistRepo.insertSongToPlaylist(fakePlaylist.id,fakeSong)).thenReturn(Future.successful(Some(fakePlaylist)))
      await(mockplaylistService.insertSongToPlaylist(fakePlaylist.id,fakeSong.id)) mustBe Some(fakePlaylist)
    }

    "if song not found return current playlist" in {
      when(mockCatalogueService.getSongFromCatalogue(fakeSong.id)).thenReturn(Future.successful(None))
      when(mockPlaylistRepo.findPlaylist(fakePlaylist.id)).thenReturn(Future.successful(Some(fakePlaylist)))
      await(mockplaylistService.insertSongToPlaylist(fakePlaylist.id,fakeSong.id)) mustBe Some(fakePlaylist)
    }
  }

}
