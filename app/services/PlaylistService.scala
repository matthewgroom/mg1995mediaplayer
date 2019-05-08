package services

import com.google.inject.Inject
import model.{Playlist, Song}
import play.api.libs.json.{JsSuccess, JsValue, Json}
import play.api.mvc.{AbstractController, Action, ControllerComponents, Result}
import reactivemongo.api.commands.{DefaultWriteResult, UpdateWriteResult}
import reactivemongo.io.netty.util.concurrent.Future
import repository.PlaylistRepo

import scala.util.Random
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class PlaylistService @Inject()(components: ControllerComponents,
                                playlistRepo: PlaylistRepo) extends AbstractController(components) {

  implicit val ec = ExecutionContext.global

  def createPlaylist(playlist: Playlist)= playlistRepo.createPlaylist(playlist).map(_.ok)

  def getPlaylist(playlistName: String) = {
    playlistRepo.findPlaylist(playlistName)
  }

  def shuffle(playlist: Playlist, random: Random):Song = {
    val songs = playlist.songs
    Random.shuffle(songs).head
  }

  def updatePlaylist(playlistName: String, newPlaylist: Playlist) = {
    playlistRepo.updatePlaylist(playlistName, newPlaylist).map {
      case Some(_) => Success
      case None    => Failure
    }
  }

  def findSongWithinPlaylist(playlist: Playlist, songName: String):List[Song] = {
    playlist.songs.filter(_.title.contains(songName))
  }

  def returnAllPlaylist() = {
    playlistRepo.findAllPlaylists()
  }

  def returnAllSongs() = {
    playlistRepo.findAllPlaylists.map(playlist => playlist.flatMap(_.songs))
  }

}
