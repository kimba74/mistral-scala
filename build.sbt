lazy val scalaSandbox = (project in file (".")).
  settings(
    name := "mistral-scala",
    version := "0.1-SNAPSHOT",
    scalaVersion := "2.11.5",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" % "akka-actor_2.11" % "2.3.9"
    )
  )
