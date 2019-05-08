package repository

import java.util.concurrent

import javax.inject.Inject
import model.Playlist
import model.Playlist._
import play.api.db
import play.api.libs.json.{JsObject, Json, Reads}
import play.api.mvc.{AbstractController, ControllerComponents, Result}
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.api.Cursor
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.BSONDocument
import reactivemongo.play.json.ImplicitBSONHandlers._
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}

class CatalogueRepo @Inject()(components: ControllerComponents,
                             val reactiveMongoApi: ReactiveMongoApi,
                            ) extends AbstractController(components) with MongoController with ReactiveMongoComponents {

  implicit def ec: ExecutionContext = components.executionContext

  implicit def collection: Future[JSONCollection] = database.map(_.collection[JSONCollection]("catalogue"))

  def createCatalogue():Future[Result] = {
    ???
  }

}
