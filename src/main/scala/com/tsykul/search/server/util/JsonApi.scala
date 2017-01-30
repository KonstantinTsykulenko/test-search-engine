package com.tsykul.search.server.util

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.tsykul.search.server.model.{Document, Query, QueryResult, Term}
import spray.json.DefaultJsonProtocol

trait JsonApi extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val documentFormat = jsonFormat2(Document)
  implicit val termFormat = jsonFormat1(Term)
  implicit val queryFormat = jsonFormat1(Query)
  implicit val queryResultFormat = jsonFormat1(QueryResult)
}
