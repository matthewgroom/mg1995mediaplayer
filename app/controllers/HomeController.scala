package controllers

import javax.inject._
import model.{Catalogue, EmptyPlaylist, Playlist, Song}
import model.Playlist._
import model.Song._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc._
import play.api.mvc.{Action, AnyContent}
import repository.{CatalogueRepo, PlaylistRepo}
import services.{CatalogueService, PlaylistService}
import play.api.data.format.Formats._
import play.api.i18n.Messages
import views.html.catalogue_of_songs
import views.html.helper.form

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}


@Singleton
class HomeController @Inject()(cc: ControllerComponents,
                               playlistService: PlaylistService,
                               catalogueService: CatalogueService,
                               playlistRepo: PlaylistRepo,
                               catalogueRepo: CatalogueRepo) extends AbstractController(cc) {
  implicit val ec = ExecutionContext.global


  def index():Action[AnyContent] = Action.async {
    playlistService.returnAllPlaylist().map(playlists => Ok(views.html.index(playlists)))
  }

  def insertCatalogue():Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      request.body.validate[Catalogue] match {
        case JsSuccess(catalogue,_) =>
          catalogueService.insertCatalogue(catalogue).map {
            case true   => Ok(Json.obj("success" -> 200))
            case false  => BadRequest(Json.obj("error" -> 400, "message" -> "Invalid Catalogue"))
          }
        case _: JsError => Future.successful(BadRequest(Json.obj("error" -> 400, "message" -> "Invalid Playlist")))
      }
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
    catalogueService.returnAllSongs().map(songs => Ok(views.html.create_playlist_page(songs,playlistForm)))
  }


  val playlistForm: Form[EmptyPlaylist] = Form(
    mapping(
      "name" -> text
    )(EmptyPlaylist.apply)(EmptyPlaylist.unapply))

  def createEmptyPlaylist(): Action[AnyContent] = Action.async {
    implicit request => {
      playlistForm.bindFromRequest().fold(
        formWithErrors => {
          Future.successful(BadRequest(views.html.error_page()))
        },
        emptyPlaylist => {
          val playlistData = Playlist.apply(emptyPlaylist.name, List.empty)
          playlistService.createPlaylist(playlistData)
          //look at DC-1629 for clarity on how to construct two different case classes into one.
          catalogueService.returnAllSongs().map(songs => Ok(views.html.catalogue_of_songs(emptyPlaylist.name,songs)))
        }
      )
    }
  }

  def insertSongToPlaylist(playlistTitle: String, songTitle: String):Action[AnyContent] = {
    ???
    //Ok()
    //Ok(views.html.error_page)
  }
}
