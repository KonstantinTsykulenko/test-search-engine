package com.tsykul.search.server.model

import akka.routing.ConsistentHashingRouter.ConsistentHashable

case class Document(key: String, document: String) extends ConsistentHashable {
  override def consistentHashKey = key
}
