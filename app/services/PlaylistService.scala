package services

import com.google.inject.Inject
import model.{Playlist, Song}
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.Future
import repository.PlaylistRepo

import scala.concurrent.ExecutionContext
import scala.util.Random

class PlaylistService @Inject()(components: ControllerComponents,
                                playlistRepo: PlaylistRepo,
                                catalogueService: CatalogueService) extends AbstractController(components) {

  implicit val ec = ExecutionContext.global

  def createPlaylist(playlist: Playlist): Future[Boolean] = playlistRepo.createPlaylist(playlist).map(_.ok)

  def getPlaylist(id: Int): Future[Option[Playlist]] = {
    playlistRepo.findPlaylist(id)
  }

  def returnAllPlaylist(): Future[Seq[Playlist]] = {
    playlistRepo.findAllPlaylists()
  }

  def insertSongToPlaylist(playlistId: Int, songId: Int): Future[Option[Playlist]] = {
    catalogueService.getSongFromCatalogue(songId).flatMap {
      case Some(song) => playlistRepo.insertSongToPlaylist(playlistId, song)
      case None       => playlistRepo.findPlaylist(playlistId)
    }
  }
}
