package com.tsykul.search.server

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.routing.ConsistentHashingRouter.ConsistentHashableEnvelope
import akka.routing.{Broadcast, FromConfig}
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.tsykul.search.server.actor.{QueryResultAggregatorActor, StorageActor}
import com.tsykul.search.server.model.{Document, Query, QueryResult, QueryWrapper}
import com.tsykul.search.server.util.JsonApi
import com.typesafe.config.ConfigFactory

import scala.concurrent.Promise
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object SearchApi extends JsonApi {

  //hardcoded for simplicity
  val nrOfInstances = 2
  val shardsPerInstance = 3
  val shardCount = nrOfInstances * shardsPerInstance

  implicit val system = ActorSystem("search-server", ConfigFactory.load("api"))
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  def main(args: Array[String]): Unit = {

    val indexerRouter = system.actorOf(FromConfig.props(Props[StorageActor]), name = "indexerRouter")

    val route =
      post {
        path("index") {
          entity(as[Document]) { document =>
            indexerRouter ! document
            complete(StatusCodes.Accepted)
          }
        }
      } ~
        post {
          path("search") {
            entity(as[Query]) { query =>
              val futureResult = queryIndex(indexerRouter, query)

              onComplete(futureResult) {
                case Success(result) =>
                  complete(result)
                case Failure(ex) =>
                  complete(StatusCodes.InternalServerError)
              }
            }
          }
        } ~
        get {
          pathPrefix("retrieve" / Remaining) { key =>
            implicit val timeout = Timeout(5 seconds)
            val future = indexerRouter ? ConsistentHashableEnvelope(key, key)
            onComplete(future) {
              case Success(result) =>
                result match {
                  case doc: Document =>
                    complete(doc)
                  case _ =>
                    complete(StatusCodes.NotFound)
                }
              case _ =>
                complete(StatusCodes.NotFound)
            }
          }
        }


    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

    println("Startup finished")
  }

  private def queryIndex(indexerRouter: ActorRef, query: Query) = {
    val result = Promise[QueryResult]()
    val aggregator = system.actorOf(Props(classOf[QueryResultAggregatorActor], shardCount, result))
    indexerRouter ! Broadcast(QueryWrapper(query, aggregator))
    result.future
  }
}
