lazy val scalaSandbox = (project in file (".")).
  settings(
    name := "mistral-scala",
    version := "0.1-SNAPSHOT",
    sbtVersion := "0.13.8",
    scalaVersion := "2.11.7",
    libraryDependencies ++= Seq(
      "com.typesafe"      %  "config"        % "1.2.1",
      "com.typesafe.akka" %% "akka-actor"    % "2.3.11",
      "org.scala-lang"    %  "scala-reflect" % "2.11.6"
    )
  )
