package com.tsykul.search.server.util

import com.tsykul.search.server.model.{Document, Term}

trait Parser {
  /**
    * Simple indexing that just splits the document by spaces, no other cleanup/analysis performed
    */
  def index(document: Document): Seq[Term] = {
    document.document.split(" ").distinct.map(Term(_))
  }
}
