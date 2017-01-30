package com.tsykul.search.server.actor

import akka.actor.ActorSystem
import akka.testkit.{TestActorRef, TestKit}
import com.tsykul.search.server.model.QueryResult
import org.scalatest.{FunSuite, FunSuiteLike}

import scala.concurrent.Promise

class QueryResultAggregatorActorTest extends TestKit(ActorSystem("search-service-test")) with FunSuiteLike {

  test("Should aggregate query results") {
    val promise = Promise[QueryResult]()
    val actorRef = TestActorRef(new QueryResultAggregatorActor(5, promise))

    def addAndAssertPartialResult(value: String): Any = {
      actorRef ! QueryResult(Set(value))
      assert(!promise.isCompleted)
    }

    for (i <- 1 to 4)
      addAndAssertPartialResult(i.toString)

    actorRef ! QueryResult(Set("5"))
    assert(promise.isCompleted)

    assert(promise.future.value.get.get == QueryResult(Set("1", "2", "3", "4", "5")))
  }

}
