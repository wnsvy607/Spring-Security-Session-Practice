## `Redis` 기반 `세션 인증` 구현 예제 프로젝트

> 목적
1. Spring Security로 소셜 혹은 일반 로그인, 로그아웃 기능 구현
2. Spring Session 으로 인증 및 인가
3. Session Storage를 3가지 방식으로 구현 
   1. 인메모리 저장(?)
   2. DB에 저장(h2)
   3. Redis에 저장

> 환경

- `Java` 17
- `Spring Boot` 3.0.6
- `Spring` 6.0.8

> 의존성
- `Spring Web`
- `Spring Data JPA`
- `Spring Data Reactive Redis`
- `Spring Security`
- `Spring Security OAuth2 Client`
- `H2 DB`
- `Lombok`
- `Spring Session`
- `Thymeleaf`

