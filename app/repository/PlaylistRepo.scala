package repository

import java.util.concurrent

import javax.inject.Inject
import model.{Playlist, Song}
import model.Playlist._
import play.api.db
import play.api.libs.json.{JsObject, Json, Reads}
import play.api.mvc.{AbstractController, ControllerComponents}
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.api.Cursor
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.BSONDocument
import reactivemongo.play.json.ImplicitBSONHandlers._
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}

class PlaylistRepo @Inject()(components: ControllerComponents,
                         val reactiveMongoApi: ReactiveMongoApi,
                        ) extends AbstractController(components) with MongoController with ReactiveMongoComponents {

  implicit def ec: ExecutionContext = components.executionContext

  implicit def collection: Future[JSONCollection] = database.map(_.collection[JSONCollection]("playLists"))

  def createPlaylist(playlist: Playlist): Future[WriteResult] = {
    collection.flatMap(_.insert.one(playlist))
  }

  def findAllPlaylists():Future[Seq[Playlist]] = {
    collection.flatMap(_.find(Json.obj(), Some(BSONDocument.empty)).cursor[Playlist]().collect[Seq](200000, Cursor.FailOnError[Seq[Playlist]]()))
  }

  def findPlaylist(id: Int): Future[Option[Playlist]] = {
    collection.flatMap(_.find(Json.obj(fields = "id" -> id), Option.empty[JsObject]).one[Playlist])
  }

  def updatePlaylistHelper(col: JSONCollection, playlist: Playlist, id: Int): Future[Option[Playlist]] = {
    val query = Json.obj(fields = "id" -> id)
    col.findAndUpdate(selector = query, update = playlist, fetchNewObject = false, upsert = true).map(_.result[Playlist])
  }

  def updatePlaylist(id: Int, playlist: Playlist): Future[Option[Playlist]] = {
    collection.flatMap(col => updatePlaylistHelper(col,playlist,id))
  }

  def deletePlaylist(playlistName: String): Future[WriteResult] = {
    val query = Json.obj(fields = "name" -> playlistName)
    collection.flatMap(_.delete.one(query))
  }

  def insertSongToPlaylist(id: Int, song: Song): Future[Option[Playlist]] = {
    val query = Json.obj(fields = "id" -> id)

    findPlaylist(id).map {
      case Some(playlist) => {
        val newSongs = song :: playlist.songs.filterNot(_.id == song.id)
        Some(playlist.copy(songs = newSongs))
      }
      case None => None
    } flatMap {
      case Some(playlist) => updatePlaylist(id, playlist)
      case None           => Future.successful(None)
    }
  }

}
