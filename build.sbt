lazy val scalaSandbox = (project in file (".")).
  settings(
    name := "mistral-scala",
    version := "0.1-SNAPSHOT",
    scalaVersion := "2.11.6",
    libraryDependencies ++= Seq(
      "com.typesafe" % "config" % "1.2.1",
      "com.typesafe.akka" %% "akka-actor" % "2.3.9"
    )
  )
