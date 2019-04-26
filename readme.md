##Imdb Web Application
A Spring boot application with rest endpoints for some queries on movies and actors
##Swagger
http://localhost:8080/swagger-ui.html
##FrontEnd
http://localhost:4200
##Database console
http://localhost:8080/h2-console,
Database is file based in directory ./database/
##Getting started
###Running tests
mvn clean package
###Start the backend
Place files in srr/main/resources (Only 3 files are needed title.basics.tsv,name.basics.tsv and title.ratings.tsv)  Note: First deployment or start might take up to 12 minutes as Spring  Batch will import to database
Start as Spring boot application and check swagger,Recommended Memory is 6GB -Xmx6G minimum is 4GB
###Start The Angular JS Frontend
Navigate to src/main/frontend and start using npm start and go to localhost:4200
##Mathematical Equation used
weighted rating (WR) = (v ÷ (v+m)) × R + (m ÷ (v+m)) × C  from https://www.quora.com/How-does-IMDbs-rating-system-work
this is the main equation works only for topo 250 movies that should have high number of votes
##Built with
Spring boot 2.1.3,Maven,Spring Data JPA, Spring Batch,H2 File database and In Memory for Test
##Example Endpoint CURL
curl -X GET "http://localhost:8080/imdb/degreeToKevinBacon?actorId=nm0000138" -H "accept: */*",
curl -X GET "http://localhost:8080/imdb/isTypeCasted?actorId=nm0000138" -H "accept: */*",
curl -X GET "http://localhost:8080/imdb/topRated/Comedy" -H "accept: */*"
and so on
##Assumptions
Files are valid from IMDB!