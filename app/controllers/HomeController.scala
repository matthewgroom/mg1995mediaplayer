package controllers

import javax.inject._
import model.{Playlist, Song}
import model.Playlist._
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc._
import repository.PlaylistRepo
import services.PlaylistService

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class HomeController @Inject()(cc: ControllerComponents, playlistService: PlaylistService, playlistRepo: PlaylistRepo) extends AbstractController(cc) {
  implicit val ec = ExecutionContext.global


  def index():Action[AnyContent] = Action.async {
    playlistService.returnAllPlaylist().map(playlists => Ok(views.html.index(playlists)))
  }

  def selectPlaylist(playlistName: String): Action[AnyContent] = Action.async {
    playlistService.getPlaylist(playlistName).map {
      case Some(playlist) => Ok(views.html.player(playlist))
      case None           => Ok(views.html.error_page())
    }
  }

  def errorPage() = Action {
    Ok(views.html.error_page())
  }

  def createPlaylist(): Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      request.body.validate[Playlist] match {
        case JsSuccess(playlist, _) =>
          playlistService.createPlaylist(playlist).map {
            case true   => Ok(Json.obj("success" -> 200))
            case false  => BadRequest(Json.obj("error" -> 400, "message" -> "Invalid Playlist"))
          }
        case _: JsError => Future.successful(BadRequest(Json.obj("error" -> 400, "message" -> "Invalid Playlist")))
      }
  }

  def createPlaylistPage():Action[AnyContent] = Action.async {
    playlistService.returnAllSongs().map(songs => Ok(views.html.create_playlist_page(songs)))
  }
}
