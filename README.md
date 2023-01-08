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
    instance-id: ${spring.cloud.client.hostname}:${spring.application.instance_id}:${random.value}
```
