package com.tsykul.search.server.util

import com.tsykul.search.server.model.{Document, Term}
import org.scalatest.FunSuite

class ParserTest extends FunSuite {

  class TestParser extends Parser

  test("Should parse by spaces") {
    val parser = new TestParser

    assert(parser.index(Document("key", "v1 v2 v3")) == Seq(Term("v1"), Term("v2"), Term("v3")))
  }

}
