package model

import play.api.libs.json.{Json, OWrites, Reads}


case class Playlist(name: String, songs: List[Song])

object Playlist {

  implicit val readsPlaylist: Reads[Playlist] = Json.reads[Playlist]
  implicit val writesPlaylist: OWrites[Playlist] = Json.writes[Playlist]
}




