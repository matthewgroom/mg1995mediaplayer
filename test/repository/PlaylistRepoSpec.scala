package repository

import model.{Catalogue, Playlist, Song}
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.test.Helpers._

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext

class PlaylistRepoSpec extends PlaySpec with MockitoSugar with GuiceOneAppPerSuite  {

  implicit val ec = ExecutionContext.global

  val mockPlaylistRepository = app.injector.instanceOf[PlaylistRepo]

  val playlist1 = Playlist(1,"name",List(Song(1,"song","album","artist name","location",1995)))
  val playlist2 = Playlist(2,"name",List(Song(2,"song","album","artist name","location",1995)))

  val song = Song(3,"song","album","matt","location",1997)

  "findAllPlaylists" should {
    "returns all playlists" in {
      await(mockPlaylistRepository.deleteAllPlaylists())
      await(mockPlaylistRepository.createPlaylist(playlist1))
      await(mockPlaylistRepository.findAllPlaylists()) mustBe List(playlist1)
    }
  }

  "findPlaylist" should {
    "return a single playlist" in {
      await(mockPlaylistRepository.deleteAllPlaylists())
      await(mockPlaylistRepository.createPlaylist(playlist1))
      await(mockPlaylistRepository.createPlaylist(playlist2))
      await(mockPlaylistRepository.findPlaylist(2)) mustBe Some(playlist2)
    }
  }

  "insertSongToPlaylist" should {
    "insert new song to existing playlist" in {
      await(mockPlaylistRepository.deleteAllPlaylists())
      await(mockPlaylistRepository.createPlaylist(playlist1))
      await(mockPlaylistRepository.insertSongToPlaylist(1,song))
    }
  }

}
