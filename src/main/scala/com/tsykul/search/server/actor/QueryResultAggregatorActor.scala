package com.tsykul.search.server.actor

import akka.actor.{Actor, PoisonPill}
import com.tsykul.search.server.model.QueryResult

import scala.concurrent.Promise
import scala.util.Success

class QueryResultAggregatorActor(shardCount: Int, resultPromise: Promise[QueryResult]) extends Actor {
  override def receive = aggregate(shardCount, Set())

  def aggregate(remainingShards: Int, results: Set[String]): Receive = {
    case result: QueryResult =>
      //last shard
      if (remainingShards == 1) {
        resultPromise.complete(Success(QueryResult(results ++ result.results)))
        self ! PoisonPill
      } else {
        context.become(aggregate(remainingShards - 1, results ++ result.results))
      }
  }
}
