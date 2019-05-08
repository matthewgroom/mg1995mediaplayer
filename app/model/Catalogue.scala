package model

import play.api.libs.json.{Json, OWrites, Reads}


case class Catalogue(songs: List[Song])

object Catalogue {

  implicit val readsCatalogue: Reads[Catalogue] = Json.reads[Catalogue]
  implicit val writesCatalogue: OWrites[Catalogue] = Json.writes[Catalogue]
}
