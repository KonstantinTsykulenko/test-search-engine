package com.tsykul.search.server.actor

import akka.actor.{Actor, ActorLogging}
import com.tsykul.search.server.model._
import com.tsykul.search.server.util.Parser

import scala.collection.mutable

class StorageActor extends Actor with Parser with ActorLogging {
  private val indexShard = new mutable.HashMap[Term, mutable.Set[String]] with mutable.MultiMap[Term, String].withDefault(_ => mutable.Set())
  private val documents = mutable.Map[String, Document]()

  def receive: Receive = {
    case doc: Document =>
      log.debug("Indexing document {}", doc)

      documents += (doc.key -> doc)

      for (term <- index(doc)) {
        indexShard.put(term, indexShard(term) += doc.key)
        log.debug("Document {} added for term {}", doc, term)
      }

      log.debug("Shard state {}", indexShard)
    case QueryWrapper(query, aggregator) =>
      log.debug("Searching index shard")
      val documents = indexShard(query.term)
      log.debug("Found documents {} for term {}", documents, query.term)
      aggregator ! QueryResult(documents.toSet)
    case key: String =>
      sender ! documents.get(key).getOrElse(NotFound(key))
  }
}
