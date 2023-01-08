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
