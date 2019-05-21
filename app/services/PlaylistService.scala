package services

import com.google.inject.Inject
import model.{Playlist, Song}
import play.api.mvc.{AbstractController, ControllerComponents}
import scala.concurrent.Future
import repository.PlaylistRepo

import scala.concurrent.ExecutionContext

class PlaylistService @Inject()(components: ControllerComponents,
                                playlistRepo: PlaylistRepo,
                                catalogueService: CatalogueService) extends AbstractController(components) {

  implicit val ec = ExecutionContext.global

  def createPlaylist(playlist: Playlist)= playlistRepo.createPlaylist(playlist).map(_.ok)

  def getPlaylist(id: Int) = {
    playlistRepo.findPlaylist(id)
  }

  def findSongWithinPlaylist(playlist: Playlist, songName: String):List[Song] = {
    playlist.songs.filter(_.title.contains(songName))
  }

  def returnAllPlaylist() = {
    playlistRepo.findAllPlaylists()
  }

  def insertSongToPlaylist(playlistId: Int, songId: Int): Future[Option[Playlist]] = {
    catalogueService.getSongFromCatalogue(songId).flatMap {
      case Some(song) => playlistRepo.insertSongToPlaylist(playlistId, song)
      case None       => playlistRepo.findPlaylist(playlistId)
    }
  }

  //  def shuffle(playlist: Playlist, random: Random):Song = {
  //    val songs = playlist.songs
  //    Random.shuffle(songs).head
  //  }
}
