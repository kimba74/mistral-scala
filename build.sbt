lazy val scalaMistral = (project in file (".")).
  settings(
    name := "mistral-scala",
    version := "0.1-SNAPSHOT",
    sbtVersion := "0.13.8",
    scalaVersion := "2.11.8",
    libraryDependencies ++= Seq(
      "com.typesafe"      %  "config"        % "1.3.0",
      "com.typesafe.akka" %% "akka-actor"    % "2.4.4",
      "org.scala-lang"    %  "scala-reflect" % "2.11.8"
    )
  )
