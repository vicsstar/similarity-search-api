package com.vigbokwe

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport.*
import akka.http.scaladsl.server.Directives.*
import akka.stream.Materializer
import spray.json.*

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContextExecutor, Promise}

// JSON Protocol
case class SearchRequest(query: String)
case class SearchResponse(similarDocument: String)

trait JsonSupport extends DefaultJsonProtocol {
  implicit val searchRequestFormat: RootJsonFormat[SearchRequest] = jsonFormat1(SearchRequest.apply)
  implicit val searchResponseFormat: RootJsonFormat[SearchResponse] = jsonFormat1(SearchResponse.apply)
}

object SimilaritySearchAPI extends JsonSupport {

  private val documents = Seq(
    "Scala is a powerful language",
    "I love programming in Scala",
    "Machine learning is fascinating",
    "Scala is functional and concise"
  )

  // Tokenizer
  def tokenize(text: String): Seq[String] = text.toLowerCase.split("\\s+").toIndexedSeq

  // Build vocabulary
  private val allTokens = documents.flatMap(tokenize).distinct

  // TF-IDF Vector
  def tfidfVector(doc: String, vocab: Seq[String]): Seq[Double] = {
    val tokenCounts = tokenize(doc).groupBy(identity).view.mapValues(_.size.toDouble)
    vocab.map { term => tokenCounts.getOrElse(term, 0.0) }
  }

  private val vectors = documents.map(doc => tfidfVector(doc, allTokens))

  // Cosine Similarity
  def cosineSimilarity(vec1: Seq[Double], vec2: Seq[Double]): Double = {
    val dotProduct = vec1.zip(vec2).map { case (x, y) => x * y }.sum
    val magnitude1 = math.sqrt(vec1.map(x => x * x).sum)
    val magnitude2 = math.sqrt(vec2.map(y => y * y).sum)
    dotProduct / (magnitude1 * magnitude2)
  }

  def findMostSimilar(query: String): String = {
    val queryVector = tfidfVector(query, allTokens)
    val similarities = vectors.map(vec => cosineSimilarity(vec, queryVector))
    val mostSimilarIndex = similarities.zipWithIndex.maxBy(_._1)._2
    documents(mostSimilarIndex)
  }

  private def startServer(): Unit = {
    implicit val system: ActorSystem = ActorSystem("SimilaritySearchAPI")
    implicit val materializer: Materializer = Materializer.matFromSystem
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    val route =
      path("search") {
        post {
          entity(as[SearchRequest]) { request =>
            val similarDoc = findMostSimilar(request.query)
            complete(SearchResponse(similarDoc))
          }
        }
      }

    val bindingFuture = Http().newServerAt("0.0.0.0", 8080).bindFlow(route)
    val shutdownPromise = Promise[Unit]()

    sys.addShutdownHook {
      Console.println("Shutting down the server gracefully...")
      bindingFuture
        .flatMap(_.unbind())
        .onComplete { _ =>
          system.terminate()
          shutdownPromise.success(())
        }
    }
    Console.println("Server online at http://0.0.0.0:8080/")
    Await.result(shutdownPromise.future, Duration.Inf)
  }

  def main(args: Array[String]): Unit = {
    startServer()
  }
}
