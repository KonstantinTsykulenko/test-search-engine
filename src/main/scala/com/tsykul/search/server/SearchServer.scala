package com.tsykul.search.server

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

import scala.io.StdIn

object SearchServer {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("search-server", ConfigFactory.load("backend"))

    println("Startup finished")
  }
}
