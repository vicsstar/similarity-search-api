FROM openjdk:11-jre-slim
LABEL authors="victor"

WORKDIR /app
COPY target/scala-3.6.4/similarity-search-api_3-0.1.0.jar /app/similarity-search-api.jar
COPY target/scala-3.6.4/lib /app/lib

#CMD ["java", "-jar", "similarity-search-api.jar"]
CMD ["java", "-cp", "similarity-search-api.jar:lib/*", "com.vigbokwe.SimilaritySearchAPI"]