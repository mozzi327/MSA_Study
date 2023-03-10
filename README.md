# MSA_Study


## π eureka client μλ‘μ΄ ν¬νΈ μΈμ€ν΄μ€ μμ±νκΈ°
### βοΈ λ°©λ² 1 
```
Intelij -> Edit Configurations -> Add VM Options -> -Dserver.port=[μνλ ν¬νΈ λ²νΈ] μ΅μ μΆκ°
```
### βοΈ λ°©λ² 2
```
./gradlew bootRun --args='--server.port=[μνλ ν¬νΈ λ²νΈ] --spring.profiles.active=local' 
```
### βοΈ λ°©λ²3
```
java -jar -Dspring.profiles.active=local -Dserver.port=[μνλ ν¬νΈ λ²νΈ] ./build/libs/user-service-0.0.1-SNAPSHOT.jar
```

### βοΈ λ°©λ²4
```
βοΈ λλ€ ν¬νΈ μ¬μ©νκΈ°
server:
  port: 0 # using random port
  
βοΈ Eureka server urlμμ μΈμ€ν΄μ€ νμΈνκΈ°(κ·Έλ₯ λλ€ ν¬νΈ μ¬μ© μ λ³΄μ΄μ§ μλλ€)
eureka:
  instance:
    instance-id: ${spring.cloud.client.hostname}:${spring.application.instance_id:${random.value}}
```

<br><br>

## π API Gateway Server Filter λ±λ‘νκΈ°

### βοΈ μ»€μ€ν Filter ν΄λμ€ μμ±
```
@Component
public class [νν° μ΄λ¦] extends AbstractGatewayFilterFactory<[νν° μ΄λ¦].[μ΄λ ν΄λμ€ μ΄λ¦]> {

    public [νν° μ΄λ¦]() {
        super([μ΄λν΄λμ€ μ΄λ¦].class);
    }

    /**
     * μ»€μ€ν νν° κ΅¬ν(λͺ¨λΈ) - yml μ€μ  μ°Έμ‘°
     * @param config μ»€μ€ν Config class
     * @return GatewayFilter
     */
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> { // λͺ¨λΈ λ°©μ(μνλ νν° λ‘μ§μ κ΅¬ννλ©΄ λλ€!)
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Custom PRE filter : request id -> {}", request.getId());

            // Custom Post Filter
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                log.info("Custom POST filter : response code -> {}", response.getStatusCode());
            }));
        };
    }

    @Getter
    public static class Config { // ymlμμ μ£Όμλ°μ Properties μ λ³΄(μ΄λ ν΄λμ€)
        // Put the configuration properties
        private final String baseMessage;
        private final boolean preLogger;
        private final boolean postLogger;

        @Builder
        public Config(String baseMessage, boolean preLogger, boolean postLogger) {
            this.baseMessage = baseMessage;
            this.preLogger = preLogger;
            this.postLogger = postLogger;
        }
    }
}
```

### βοΈ yml Filter μ€μ 
```
spring:
  application:
    name: [κ²μ΄νΈμ¨μ΄ μλ² λͺ]
  cloud:
    gateway:
      default-filters: # κΈλ‘λ² νν°λ₯Ό μ μ©ν  μ μλ€!
        - name: [κΈλ‘λ² νν° λͺ]
          args:
            [κΈλ‘λ² νν°μ λ€μ΄κ° νλ‘νΌν° 1]: Spring Cloud Gateway Global Filter
            [κΈλ‘λ² νν°μ λ€μ΄κ° νλ‘νΌν° 2]: true
            [κΈλ‘λ² νν°μ λ€μ΄κ° νλ‘νΌν° 3]: true
      routes:
        - id: first-service
          uri: lb://MY-FIRST-SERVICE
          predicates:
            - Path=/first-service/** # ν΄λΌμ΄μΈνΈ μλΉμ€ μμ²­ μ£Όμ1
          filters:
            - [νν°ν΄λμ€ μ΄λ¦]
        - id: second-service
          uri: lb://MY-SECOND-SERVICE
          predicates:
            - Path=/second-service/** # ν΄λΌμ΄μΈνΈ μλΉμ€ μμ²­ μ£Όμ2
          filters:
            - name: [νν°ν΄λμ€1 μ΄λ¦]
            - name: [νν°ν΄λμ€2 μ΄λ¦]
              args:
                [νν°ν΄λμ€2μ λ€μ΄κ° νλ‘νΌν° 1]: Hi, there.
                [νν°ν΄λμ€2μ λ€μ΄κ° νλ‘νΌν° 2]: true
                [νν°ν΄λμ€2μ λ€μ΄κ° νλ‘νΌν° 3]: true
```

<br><br>

## π Eureka Serverμ API Gateway Server μ°λ
```
spring:
  application:
    name: ...
  cloud:
    gateway:
      default-filters: ...
      routes:
        - id: first-service
          uri: lb://[MY-FIRST-SERVICE -> Eureka λμ¬λ³΄λ μΈμ€ν΄μ€λͺ]
```
