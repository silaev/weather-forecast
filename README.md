#HTTP API for a small weather forecast application serving the purpose of the analysing average 
#day/night temperatures and pressure. 

####General info
The application lets:
- get the full list of cities as per counties (to avoid mistakes) via 
`http://localhost:8080/api/v1/weather/cities`. Since such a list is a huge one, the app
makes use of Spring caching. 

- get analytics for day/night temperatures and pressure for the last `config.last-days` days 
(see application.yml) by `http://localhost:8080/api/v1/weather/averages/{cityCountryName}`. 
For instance, `cityCountryName` is `London,GB`. You can get a proper value by means of the previous
endpoint. 

While getting analytics, the app verifies that `cityCountryName` is in 
the full list of cities as per counties in order to reduce additional 
workload on a downstream service that has some limitations on the number of the requests.
Such a verification employs Spring caching as well to reduce a further request time execution.
On top of that, a getting analytics endpoint leverages Spring caching by 
a cityCountryName and a threshold (a moment of time from now to now plus config.last-days).  
As an expiration time span, the app uses 3600s (see application.yml).        
          
####Requirements to consider before using the app 
1. The project ought to be built by means of Gradle. For that reason, run `gradlew clean build`.
Subsequently, to start the application make use of `gradlew bootRun`.
2. Even though integration tests are not currently implemented, all the basic 
REST operations are covered by unit ones.

####Future improvements
1. Recon the use of Spring Security Oath2 and a probational key-value storage for caching
(like Redis, Hazelcast). 
2. Never use application.yml for storing sensitive data in a production environment.
For that reason, consider either secured storage or VM options during build time
on a CI/CD server.    

#####Environment
Have a good look at Docker and put this app into a container. This approach
enjoys considerable use in modern development with regard to
product delivery process in Microservice Architecture.
Today it's only a matter of additional gradle plugin.    
        