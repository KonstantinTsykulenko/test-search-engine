package com.tsykul.search.server.model

import akka.actor.ActorRef

case class QueryWrapper(query: Query, aggregator: ActorRef)
