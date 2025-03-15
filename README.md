# Similarity Search API

This is a simple API that helps find similar documents based on a search query. It uses a mathematical method called **cosine similarity** to compare the query with a set of predefined documents.

The application is built with **Akka HTTP**, which makes it fast and scalable. It also uses **TF-IDF** (Term Frequency-Inverse Document Frequency) to convert text documents into numerical vectors that can be compared easily.

## Features

- **Search documents**: You can send a search query and get the most similar document from a list of predefined documents.
- **Cosine similarity**: The API uses cosine similarity to find the similarity between documents.
- **Akka HTTP**: The API is built with Akka HTTP, which is a fast, non-blocking web server.
- **JSON format**: The requests and responses are in JSON format.

## API Documentation

### `POST /search`

This is the endpoint where you send a search query. It will return the most similar document based on the query.

**Request Body** (JSON format):
```json
{
  "query": "your search text here"
}
```

```json
{
"similarDocument": "The most similar document"
}
```

### Example

#### 1. Request:

```json
{
"query": "Scala is awesome"
}
```

#### 2. Response:

```json
{
  "similarDocument": "Scala is a powerful language"
}
```

## How It Works

1. **Documents**: There is a set of predefined documents that the API compares to your search query. These documents include:

   * "Scala is a powerful language"
   * "I love programming in Scala"
   * "Machine learning is fascinating"
   * "Scala is functional and concise"

2. **TF-IDF**: Each document is turned into a numerical vector using TF-IDF. This helps measure how important each word is in a document compared to the whole set.

3. **Cosine Similarity**: When you send a query, it gets converted into a vector too. Then, the system calculates the cosine similarity between the query's vector and the vectors of the documents. The document that is most similar to your query is returned.

## How to Run
### Using `sbt`:

1. Clone this repository to your local machine.
2. Go to the project directory.
3. Run the project with sbt run:
```
sbt run
```
The API will be available at http://localhost:8080/.

### Using Docker:

1. Build the Docker image by running:
```
docker-compose up --build
```
2. The server will be available at http://localhost:8080/.

### Example docker-compose.yml:

A sample docker-compose.yml file is provided in the repository. You can use it to run the application with Docker Compose.

## Dependencies

* **Akka HTTP**: This is used to handle HTTP requests.
* **Spray JSON**: This is used to convert data to and from JSON format.
* **Akka ActorSystem**: This is used for managing the application’s concurrency.

## License

This project is licensed under the [MIT License](./LICENSE). See the LICENSE file for more information.

