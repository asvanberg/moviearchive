name := "moviearchive"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache
)

scalacOptions += "-feature"

play.Project.playScalaSettings
