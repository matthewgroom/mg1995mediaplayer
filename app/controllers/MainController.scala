package controllers

import javax.inject._
import model.{Catalogue, EmptyPlaylist, Playlist, Song}
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, AnyContent, _}
import repository.{CatalogueRepo, PlaylistRepo}
import services.{CatalogueService, PlaylistService}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random


@Singleton
class MainController @Inject()(cc: ControllerComponents,
                               playlistService: PlaylistService,
                               catalogueService: CatalogueService,
                               playlistRepo: PlaylistRepo,
                               catalogueRepo: CatalogueRepo
                              ) extends AbstractController(cc) {
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

  def selectPlaylist(playlistId: Int): Action[AnyContent] = Action.async {
    playlistService.getPlaylist(playlistId).map {
      case Some(playlist) => Ok(views.html.player(playlist))
      case None           => Ok(views.html.error_page())
    }
  }

  def errorPage() = Action {
    Ok(views.html.error_page())
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
        (formWithErrors: Form[EmptyPlaylist]) => {
          Future.successful(BadRequest(views.html.error_page()))
        },
        emptyPlaylist => {
          val playlistData = Playlist.apply(Random.nextInt(),emptyPlaylist.name, List.empty)
          playlistService.createPlaylist(playlistData)
          catalogueService.returnAllSongs().map(songs => Ok(views.html.catalogue_of_songs(playlistData,songs)))
        }
      )
    }
  }

  def insertSongToPlaylist(playlistId: Int, songId: Int): Action[AnyContent] = Action.async {
    for {
      playlist <- playlistService.insertSongToPlaylist(playlistId, songId).map(_.get)
      catalogueSongs <- catalogueService.returnAllSongs()
      songs = catalogueSongs.diff(playlist.songs)
    } yield Ok(views.html.catalogue_of_songs(playlist, songs))
  }

  def listViaAlbum(playlistId: Int): Action[AnyContent] = Action.async {
    playlistService.getPlaylist(playlistId).map {
      case Some(playlist) => Ok(views.html.list_via_album_title(playlist,playlist.songs.sortBy(_.album)))
      case None           => Ok(views.html.error_page())
    }
  }

  def listViaSong(playlistId: Int): Action[AnyContent] = Action.async {
    playlistService.getPlaylist(playlistId).map {
      case Some(playlist) => Ok(views.html.list_via_song_title(playlist,playlist.songs.sortBy(_.title)))
      case None           => Ok(views.html.error_page())
    }
  }

  def listViaRandom(playlistId: Int): Action[AnyContent] = Action.async {
    playlistService.getPlaylist(playlistId).map{
      case Some(playlist) => Ok(views.html.list_via_randomly(playlist,Random.shuffle(playlist.songs)))
      case None           => Ok(views.html.error_page())
    }
  }
}
