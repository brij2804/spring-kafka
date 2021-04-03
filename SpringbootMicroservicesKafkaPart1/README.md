# Springboot Kafka Microservices project
Springboot Kafka Microservices code

## Tech Stack 
- java11
- twitter4j
- springboot-starter 2.4.4
- spring-cloud 2020.0.2
- H2 database
- spring cloud config server
- spring cloud config client 
- feign client
- Eureka Server
- Eureka Client
- Spring Cloud LoadBalancer
- Spring Cloud API Gateway
- Resilience4j circuit breaker

## Modules
- microservices-kafka
- twitter-to-kafka-service
- spring-cloud-config-server

### Configuration Management
- Spring Cloud Config Server
### Dynamic Scale Up and Down
- Naming Server : Eureka
- Load Balancing : Spring Cloud LoadBalancer
- Rest clients : Feign 
### Visibility and Monitoring
- Distributed Tracing : Zipkin and Spring Cloud Sleuth
- API Gateway : Spring Cloud API Gateway 
### Fault Tolerance
- Resilience4j

## Ports
| Application      | Port          | 
| -------------    |:-------------:| 
| Limits Service   | 8080, 8081, ...| 
| Spring Cloud Config Server  | 8888   | 
| Currency Exchange Service | 8000, 8001, 8002, ..  | 
| Currency Conversion Service | 8100, 8101, 8102, ... |
| Netflix Eureka Naming Server | 8761 |
| API Gateway Server | 8765 |
| Zipkin Distributed Tracing Server | 	9411 |

## URLs
|Application                                      | URL|
| :----------------------------------------------|:-------------|
|Limits Service                                   | http://localhost:8080/limits http://localhost:8080/actuator/refresh (POST)|
|Spring Cloud Config Server                       | http://localhost:8888/limits-service/default http://localhost:8888/limits-service/dev|
|Currency Converter Service - Direct Call         | http://localhost:8100/currency-converter/from/USD/to/INR/quantity/10 |
|Currency Converter Service - Feign               | http://localhost:8100/currency-converter-feign/from/EUR/to/INR/quantity/10000 |
|Currency Exchange Service                        | http://localhost:8000/currency-exchange/from/EUR/to/INR http://localhost:8001/currency-exchange/from/USD/to/INR |
|Eureka	                                          | http://localhost:8761/|
|API Gateway - Currency Exchange & Exchange Services     | http://localhost:8765/currency-exchange-service/currency-exchange/from/EUR/to/INR  http://localhost:8765/currency-conversion-service/currency-converter-feign/from/USD/to/INR/quantity/10|
|Zipkin	                                          | http://localhost:9411/zipkin/|
|Spring Cloud Bus Refresh                         | http://localhost:8080/actuator/bus-refresh (POST)|

### commands
- docker-compose -f common.yml -f kafka_cluster.yml up









