package model

import play.api.libs.json.{Json, OWrites, Reads}


case class EmptyPlaylist(name: String)

object EmptyPlaylist {

  implicit val readsPlaylist: Reads[EmptyPlaylist] = Json.reads[EmptyPlaylist]
  implicit val writesPlaylist: OWrites[EmptyPlaylist] = Json.writes[EmptyPlaylist]
}

