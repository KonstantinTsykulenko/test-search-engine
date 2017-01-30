package com.tsykul.search.server.actor

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import com.tsykul.search.server.model._
import org.scalatest.FunSuiteLike

class StorageActorTest extends TestKit(ActorSystem("search-service-test")) with FunSuiteLike with ImplicitSender {

  test("Should index documents") {
    val actorRef = TestActorRef[StorageActor]

    actorRef ! Document("k1", "v1 v2 v3")
    actorRef ! Document("k2", "v2 v3 v4")
    actorRef ! Document("k3", "v3 v4 v5")

    def assertQuery(value: String, documents: Set[String]) = {
      actorRef ! QueryWrapper(Query(Term(value)), self)
      expectMsgPF() {
        case result: QueryResult =>
          assert(result.results == documents)
      }
    }

    assertQuery("v1", Set("k1"))
    assertQuery("v2", Set("k1", "k2"))
    assertQuery("v3", Set("k1", "k2", "k3"))
    assertQuery("v4", Set("k2", "k3"))
    assertQuery("v5", Set("k3"))
  }

  test("Should store documents") {
    val actorRef = TestActorRef[StorageActor]

    val document = Document("k1", "v1")
    actorRef ! document

    actorRef ! "k1"
    expectMsgPF() {
      case result: Document =>
        assert(document == result)
    }

    actorRef ! "k2"
    expectMsgClass(classOf[NotFound])
  }
}
