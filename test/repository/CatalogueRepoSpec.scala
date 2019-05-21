package repository

import akka.http.scaladsl.model.HttpHeader.ParsingResult.Ok
import akka.stream.Materializer
import model.Playlist
import org.openqa.selenium.remote.RemoteWebDriver.When
import org.scalatest._
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.inject.guice.GuiceApplicationBuilder
import services.{CatalogueService, PlaylistService}
import org.scalatestplus.play._
import org.mockito.Mockito._
import play.api.Application
import play.api.libs.json.Json
import play.api.mvc.DefaultActionBuilder
import play.api.test.Helpers

import scala.concurrent.Future

class CatalogueRepoSpec extends PlaySpec with MockitoSugar with GuiceOneAppPerSuite {


}
