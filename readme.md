# MyVideo

## 사용자 동영상 공유 플랫폼

- 백엔드 : 자바 스프링
- 프론트엔드 : 뷰.js

## Trouble Shooting
### Intellij Http Client 응답 변수 설정하기
```http request
### Post Login Success
POST http://localhost:8080/api/v1/user/login
Content-Type: application/json

{
    "email" : "qwe@qwe.com",
    "password" : "qweqweqwe"
}

> {%
    // Authorization 헤더 값을 가져와 전역 변수에 저장
    client.global.set("AUTH_HEADER", response.headers.valueOf("Authorization"));
    console.log("Saved Authorization Header:", response.headers); // 응답 헤더 모두 출력하기
%}


### Get userinfo
GET http://localhost:8080/api/v1/user/getInfo
Authorization: {{AUTH_HEADER}} // 응답으로 저장된 변수 사용하기
```