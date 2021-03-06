package model

import play.api.libs.json.Json

case class Song(id: Int, title: String, album: String, artistName: String, location: String, yearCreated: Int)

object Song {

  implicit val format = Json.format[Song]
}
