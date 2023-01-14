# MSA_Study


## 📌 eureka client 새로운 포트 인스턴스 생성하기
### ✔️ 방법 1 
```
Intelij -> Edit Configurations -> Add VM Options -> -Dserver.port=[원하는 포트 번호] 옵션 추가
```
### ✔️ 방법 2
```
./gradlew bootRun --args='--server.port=[원하는 포트 번호] --spring.profiles.active=local' 
```
### ✔️ 방법3
```
java -jar -Dspring.profiles.active=local -Dserver.port=[원하는 포트 번호] ./build/libs/user-service-0.0.1-SNAPSHOT.jar
```

### ✔️ 방법4
```
❗️ 랜덤 포트 사용하기
server:
  port: 0 # using random port
  
❗️ Eureka server url에서 인스턴스 확인하기(그냥 랜덤 포트 사용 시 보이지 않는다)
eureka:
  instance:
    instance-id: ${spring.cloud.client.hostname}:${spring.application.instance_id:${random.value}}
```

<br><br>

## 📌 API Gateway Server Filter 등록하기

### ✔️ 커스텀 Filter 클래스 작성
```
@Component
public class [필터 이름] extends AbstractGatewayFilterFactory<[필터 이름].[이너 클래스 이름]> {

    public [필터 이름]() {
        super([이너클래스 이름].class);
    }

    /**
     * 커스텀 필터 구현(모노) - yml 설정 참조
     * @param config 커스텀 Config class
     * @return GatewayFilter
     */
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> { // 모노 방식(원하는 필터 로직을 구현하면 된다!)
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
    public static class Config { // yml에서 주입받을 Properties 정보(이너 클래스)
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

### ✔️ yml Filter 설정
```
spring:
  application:
    name: [게이트웨이 서버 명]
  cloud:
    gateway:
      default-filters: # 글로벌 필터를 적용할 수 있다!
        - name: [글로벌 필터 명]
          args:
            [글로벌 필터에 들어갈 프로퍼티 1]: Spring Cloud Gateway Global Filter
            [글로벌 필터에 들어갈 프로퍼티 2]: true
            [글로벌 필터에 들어갈 프로퍼티 3]: true
      routes:
        - id: first-service
          uri: lb://MY-FIRST-SERVICE
          predicates:
            - Path=/first-service/** # 클라이언트 서비스 요청 주소1
          filters:
            - [필터클래스 이름]
        - id: second-service
          uri: lb://MY-SECOND-SERVICE
          predicates:
            - Path=/second-service/** # 클라이언트 서비스 요청 주소2
          filters:
            - name: [필터클래스1 이름]
            - name: [필터클래스2 이름]
              args:
                [필터클래스2에 들어갈 프로퍼티 1]: Hi, there.
                [필터클래스2에 들어갈 프로퍼티 2]: true
                [필터클래스2에 들어갈 프로퍼티 3]: true
```

<br><br>

## 📌 Eureka Server와 API Gateway Server 연동
```
spring:
  application:
    name: ...
  cloud:
    gateway:
      default-filters: ...
      routes:
        - id: first-service
          uri: lb://[MY-FIRST-SERVICE -> Eureka 대쉬보드 인스턴스명]
```
