import sbt._
import Keys._
import org.scalatra.sbt._
import org.scalatra.sbt.PluginKeys._
import com.mojolly.scalate.ScalatePlugin._
import ScalateKeys._

object LaundryapiBuild extends Build {
  val Organization = "com.github.baocin"
  val Name = "LaundryAPI"
  val Version = "0.1.0-SNAPSHOT"
  val ScalaVersion = "2.11.6"
  val ScalatraVersion = "2.4.0.RC3"

  lazy val project = Project (
    "laundryapi",
    file("."),
    settings = ScalatraPlugin.scalatraSettings ++ scalateSettings ++ Seq(
      organization := Organization,
      name := Name,
      version := Version,
      scalaVersion := ScalaVersion,
      resolvers += Classpaths.typesafeReleases,
      resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",
      libraryDependencies ++= Seq(
        "org.scalatra" %% "scalatra" % ScalatraVersion,
        "org.scalatra" %% "scalatra-scalate" % ScalatraVersion,
        "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",
        "ch.qos.logback" % "logback-classic" % "1.1.2" % "runtime",
        "org.eclipse.jetty" % "jetty-webapp" % "9.2.10.v20150310" % "container",
        "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",
        "org.scalatra" %% "scalatra-swagger"  % "2.4.0.RC1",
        "org.jsoup" % "jsoup" % "1.8.3" from "http://jsoup.org/packages/jsoup-1.8.3.jar",
        "org.json4s" % "json4s-native_2.11" % "3.3.0",
        "org.json4s" % "json4s-jackson_2.11" % "3.3.0",
        "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.6.3",
        "com.fasterxml.jackson.core" % "jackson-annotations" % "2.6.3",
        "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.3",
        "com.fasterxml.jackson.core" % "jackson-core" % "2.6.3",
        "com.typesafe.slick" %% "slick" % "3.1.0",
        "org.slf4j" % "slf4j-nop" % "1.6.4",
	"com.typesafe.slick" %% "slick-codegen" % "3.1.0",
	"mysql" % "mysql-connector-java" % "5.1.38"
      ),
      scalateTemplateConfig in Compile <<= (sourceDirectory in Compile){ base =>
        Seq(
          TemplateConfig(
            base / "webapp" / "WEB-INF" / "templates",
            Seq.empty,  /* default imports should be added here */
            Seq(
              Binding("context", "_root_.org.scalatra.scalate.ScalatraRenderContext", importMembers = true, isImplicit = true)
            ),  /* add extra bindings here */
            Some("templates")
          )
        )
      }
    )
  )
}
