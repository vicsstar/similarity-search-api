ThisBuild / version := "0.1.0"

ThisBuild / scalaVersion := "3.6.4"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.5.3",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.5.3",
  "com.typesafe.akka" %% "akka-stream" % "2.8.8",
  "io.spray" %% "spray-json" % "1.3.6",
)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.19" % Test
)

scalacOptions ++= Seq("-deprecation")

lazy val copyDeps = taskKey[Unit]("Copy dependencies to a directory")

copyDeps := {
  val targetDir = target.value / ("scala-" + scalaVersion.value) / "lib"  // Define the target directory for dependencies
  val jarDeps = (Compile / dependencyClasspath).value  // Get all dependencies

  // Create the target directory if it doesn't exist
  if (!targetDir.exists()) targetDir.mkdirs()

  // Copy all JAR dependencies to the target directory
  jarDeps.foreach { dep =>
    val destFile = targetDir / dep.data.getName
    if (!destFile.exists()) {
      IO.copyFile(dep.data, destFile)
    }
  }

  println(s"Copied dependencies to: $targetDir")
}

lazy val root = (project in file("."))
  .settings(
    name := "similarity-search-api",
    idePackagePrefix := Some("com.vigbokwe"),
    Compile / packageBin / mainClass := Some("com.vigbokwe.SimilaritySearchAPI"),
    Compile / packageBin / packageOptions += Package.ManifestAttributes(
      "Main-Class" -> "com.vigbokwe.SimilaritySearchAPI"
    ),
    Compile / packageBin := {
      val jarFile = (Compile / packageBin).value  // Package the JAR first
      copyDeps.value  // Then copy the dependencies
      jarFile // Return the JAR file for further use
    }
)