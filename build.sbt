name := "mediaplayer"
 
version := "1.0" 
      
lazy val `mediaplayer` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
      
scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  jdbc ,
  ehcache ,
  ws ,
  specs2 % Test ,
  guice,
  "org.reactivemongo" %% "play2-reactivemongo" % "0.16.4-play26",
  "com.typesafe.play" %% "play-json" % "2.7.1",
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.0.0" % "test"
)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

      