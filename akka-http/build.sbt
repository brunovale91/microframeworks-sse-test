lazy val akkaHttpVersion = "10.2.0"
lazy val akkaVersion    = "2.6.8"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "org.swarmbit",
      scalaVersion := "2.13.1",
      name := "akka-http-sse"
    )),
    name := "TestProject",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-actor-typed"     % akkaVersion,
      "com.typesafe.akka" %% "akka-stream"          % akkaVersion,
      "com.typesafe.akka" %% "akka-http-jackson"    % akkaHttpVersion,
      "ch.qos.logback"    % "logback-classic"       % "1.2.3",

      "com.typesafe.akka" %% "akka-testkit"                 % akkaVersion     % Test,
      "com.typesafe.akka" %% "akka-http-testkit"            % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-actor-testkit-typed"     % akkaVersion     % Test,
      "junit"              % "junit"                        % "4.12"          % Test,
      "com.novocode"       % "junit-interface"              % "0.10"          % Test
    ),

    testOptions += Tests.Argument(TestFrameworks.JUnit, "-v")
  )
