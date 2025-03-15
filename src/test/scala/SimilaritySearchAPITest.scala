package com.vigbokwe

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class SimilaritySearchAPITest extends AnyFlatSpec with Matchers {
  "Tokenizer" should "split text into words" in {
    SimilaritySearchAPI.tokenize("Scala is awesome") shouldEqual Seq("scala", "is", "awesome")
  }

  "TF-IDF Vector" should "create a vector for the document" in {
    val vocab = Seq("scala", "is", "awesome")
    SimilaritySearchAPI.tfidfVector("Scala is awesome", vocab) shouldEqual Seq(1.0, 1.0, 1.0)
  }

  "Cosine Similarity" should "return 1 for identical vectors" in {
    val vec = Seq(1.0, 1.0, 1.0)
    SimilaritySearchAPI.cosineSimilarity(vec, vec) shouldEqual 1.0000000000000002
  }

  "Find most similar document" should "identify the closest match" in {
    SimilaritySearchAPI.findMostSimilar("I enjoy coding in Scala") shouldEqual "I love programming in Scala"
  }
}
