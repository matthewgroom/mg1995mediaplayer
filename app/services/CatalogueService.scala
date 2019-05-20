package services

import com.google.inject.Inject
import model.{Catalogue, Song}
import play.api.mvc.{AbstractController, ControllerComponents}
import repository.CatalogueRepo

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Random, Success}
import scala.concurrent.ExecutionContext

class CatalogueService @Inject()(components: ControllerComponents,
                                 catalogueRepo: CatalogueRepo) extends AbstractController(components) {

  implicit val ec = ExecutionContext.global

  def insertCatalogue(catalogue: Catalogue) = catalogueRepo.createCatalogue(catalogue).map(_.ok)

  def returnAllSongs()= {
    catalogueRepo.returnCatalogue().map(_.flatMap(_.songs))
  }

  def returnSingleSong(songTitle: String) = {
    catalogueRepo.returnCatalogue().map(_.flatMap(_.songs).filter(_.title.contentEquals(songTitle)).head)
  }

}
