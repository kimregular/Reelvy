# MyVideo

<!-- TOC -->
* [MyVideo](#myvideo)
  * [사용자 동영상 공유 플랫폼](#사용자-동영상-공유-플랫폼)
  * [Trouble Shootings](#trouble-shootings)
    * [Intellij Http Client 응답 변수 설정하기](#intellij-http-client-응답-변수-설정하기)
    * [AXIOS 응답 헤더에 Authorization 없는 문제](#axios-응답-헤더에-authorization-없는-문제)
    * [로그인 후 새로고침하면 로그아웃되던 문제](#로그인-후-새로고침하면-로그아웃되던-문제)
    * [jwt 요청 방법 설정](#jwt-요청-방법-설정)
    * [로그인한 유저가 /login 페이지 접근하던 문제](#로그인한-유저가-login-페이지-접근하던-문제)
    * [비디오 업로드 요청이 백엔드에서 거절되던 문제](#비디오-업로드-요청이-백엔드에서-거절되던-문제)
    * [Admin은 비디오 업로드가 안 되던 현상](#admin은-비디오-업로드가-안-되던-현상)
  * [의사결정 기록](#의사결정-기록)
    * [Util 클래스들에 @Component 사용](#util-클래스들에-component-사용)
    * [서비스단에는 private 클래스가 없어야한다!](#서비스단에는-private-클래스가-없어야한다)
    * [프론트에서 쿠키에 접근하지 못하도록 HTTPONLY 설정 진행](#프론트에서-쿠키에-접근하지-못하도록-httponly-설정-진행)
  * [리팩토링](#리팩토링)
    * [1. 유저 프로필 사진 저장 로직 개선](#1-유저-프로필-사진-저장-로직-개선)
    * [2. 평문처럼 읽히는 코드를 만들고 싶다!](#2-평문처럼-읽히는-코드를-만들고-싶다-)
    * [3. SALT 추가 로직 테스트하기](#3-salt-추가-로직-테스트하기)
    * [4. Vue.js Component와 View 분리하기](#4-vuejs-component와-view-분리하기)
    * [5. HttpOnly 쿠키로 jwt 발급, 사용하기](#5-httponly-쿠키로-jwt-발급-사용하기)
<!-- TOC -->

## 사용자 동영상 공유 플랫폼

- 백엔드 : 자바 스프링
- 프론트엔드 : 뷰.js

## Trouble Shootings

### Intellij Http Client 응답 변수 설정하기

```http request
### Post Login Success
POST http://localhost:8080/v1/users/login
Content-Type: application/json

{
    "email" : "qwe@qwe.com",
    "password" : "qweqweqwe"
}

> {%
    // Authorization 헤더 값을 가져와 전역 변수에 저장
    client.global.set("AUTH_HEADER", response.headers.valueOf("Authorization"));
    console.log("Saved Authorization AppHeader:", response.headers); // 응답 헤더 모두 출력하기
%}


### Get userinfo
GET http://localhost:8080/v1/users/getInfo
Authorization: {{AUTH_HEADER}} // 응답으로 저장된 변수 사용하기
```

### AXIOS 응답 헤더에 Authorization 없는 문제

CorsConfig 설정

```java

@Configuration
public class CorsConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOriginPatterns("http://localhost:5173")
				.allowedMethods("*")
				.exposedHeaders("Authorization", "Content-Type")
				// 노출시킬 헤더명을 명시
				.allowCredentials(true);
	}
}
```

### 로그인 후 새로고침하면 로그아웃되던 문제

로그인 후 pinia에 토큰을 저장한 후 새로고침하면 로그아웃됨
새로고침하면 브라우저 메모리가 초기화되기 때문임

서버에서 발급받은 jwt를 쿠키에 저장한 후 요청할 때마다 쿠키에 저장된 토큰을 헤더에
넣기로 결정

```ts
// useAuthStore.ts
import {defineStore} from "pinia";
import {
    setCookie,
    getCookie,
    deleteCookie
} from "@/utils/cookieUtil.ts";

export const useAuthStore = defineStore("auth", {
    state: () => ({
        token: getCookie("authToken"), // 쿠키에서 토큰을 초기화
    }),
    actions: {
        setToken(token: string) {
            this.token = token;
            setCookie("authToken", token, new Date()); // 쿠키에 3시간 저장
        },
        clearToken() {
            this.token = null;
            deleteCookie("authToken"); // 쿠키 삭제
        },
    },
});

```

```ts
// cookieUtil.ts
export const setCookie = (name: string, value: string, now: Date) => {
    const expires = new Date(now.getTime() + 1000 * 60 * 60 * 3); // 3시간 뒤
    document.cookie = `${name}=${value};expires=${expires.toUTCString()};path=/`;
};

export const getCookie = (name: string): string | null => {
    const cookieArr = document.cookie.split("; ");
    for (const cookie of cookieArr) {
        const [key, val] = cookie.split("=");
        if (key === name) {
            return val;
        }
    }
    return null;
};

export const deleteCookie = (name: string) => {
    document.cookie = `${name}=;expires=Thu, 01 Jan 1970 00:00:01 GMT;path=/`;
};

```

### jwt 요청 방법 설정

쿠키에 저장한 jwt를 헤더에 넣어 요청

```ts
try {
    const token = document.cookie.split("=")[1];
    console.log(token);
    const response = await axios.get(`${BASE_URL}/v1/users/getInfo`, {
        headers: {
            Authorization: token,
        }
    });
    console.log(response);
    welcome.value = response.data;
} catch (error) {
    welcome.value = "error has occured";
}
```

### 로그인한 유저가 /login 페이지 접근하던 문제

vue의 라우터에서 클라이언트가 로그인했는지 확인하는 로직 추가

```ts
[{
    path: "/login",
    name: "LOGIN",
    component: LoginVue,
    beforeEnter: (to, from, next) => {
        const authStore = useAuthStore();
        if (authStore.token) {
            // 로그인한 상태면 홈 페이지로 리다이렉트
            next({path: "/"});
        } else {
            next(); // 로그인하지 않았으면 /login 접근 허용
        }
    }
}]
```

### 비디오 업로드 요청이 백엔드에서 거절되던 문제

프론트에서 요청을 하면 백엔드에서 아래 로그가
나오며 [415 코드](https://developer.mozilla.org/ko/docs/Web/HTTP/Status/415)
가 반환되었다.

`Resolved [org.springframework.web.HttpMediaTypeNotSupportedException: Content-Type 'application/json' is not supported]`

처리하는 컨트롤러에서 `MULTIPART_FORM_DATA_VALUE` 만 입력받기 때문에
발생한 현상이었다.

파일을 json 객체로 전달하면 서버가 이를 파일로 처리하지 않고, 텍스트 데이터로
처리한다.

파일을 업로드 할 때에는 formData를 사용해야한다.
`FormData` 객체는 `multipart/form-data` 형식으로 데이터를 전송할
수 있도록 도와준다.

```ts
// 수정 전
const payload = {
    videoFile: videoFile.value,
    title: videoTitle.value,
    desc: videoDesc.value
}

try {
    let authStore = useAuthStore();
    await axios.post(`${BASE_URL}/v1/videos/upload`, payload, {
        headers: {
            Authorization: authStore.token
        },
        withCredentials: true
    });
    await router.replace({name: "HOME"})
} catch (error) {
    alert("error!");
    return;
}

```

```ts
// 수정 후
const formData = new FormData();
formData.append("videoFile", videoFile.value as Blob);
formData.append("title", videoTitle.value);
formData.append("desc", videoDesc.value);

try {
    let authStore = useAuthStore();
    await axios.post(`${BASE_URL}/v1/videos/upload`, formData, {
        headers: {
            Authorization: authStore.token
        },
        withCredentials: true
    });
    await router.replace({name: "HOME"})
} catch (error) {
    alert("error!");
    return;
}
```

### Admin은 비디오 업로드가 안 되던 현상

권한이 `user`인 멤버는 비디오 업로드가 가능하지만
`admin`인 멤버는 forbidden 으로 거절되는 현상 해결

```java

@User
@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<VideoResponse> uploadVideo(@ModelAttribute @Valid VideoUploadRequest videoUploadRequest,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
	return ResponseEntity.status(HttpStatus.CREATED).body(videoService.uploadVideo(videoUploadRequest, userDetails));
}
```

업로드 컨트롤러에서 `@User` 에너테이션으로 멤버의 권한이 `User`인지 확인하는
로직이 있었기 때문에 발생한 에러

User만 확인하기 때문에 User는 업로드가 가능하지만 Admin은 업로드가 불가능했음

권한에 계층을 주어서 Admin이면 User의 모든 기능을 사용할 수 있도록 해결함

```java
// WebSecurityConfig.java

@Bean
public RoleHierarchy roleHierarchy() {
	return RoleHierarchyImpl.fromHierarchy("ROLE_ADMIN > ROLE_USER");
}
```

## 의사결정 기록

### Util 클래스들에 @Component 사용

차후 테스트코드 작성시 mock 활용을 위해 사용했음(현재 테스트 커버리지가 매우 낮다는
불상사는 차치함)

### 서비스단에는 private 클래스가 없어야한다!

서비스가 서비스에 의존하게 되면 로직 흐름 파악하기가 너무 어려움
서비스단에서 사용하는 프라이빗 메서드가 많아지면 따로 분리하는 방안이 이상적임
따라서 유틸 클래스를 만들어서 서비스단에 필요한 기타 등등 기능들은 모두 유틸 안에
정의하고 사용하도록 함

이렇게 하면 좋은점이 해당 기능 테스트를 진행할 수 있음 (프라이빗 메서드는 테스트를
작성하지 않는다) 물론 mock 사용도 가능함

유틸 클래스가 유틸 클래스 자신에 의존하는 경우에 대해서는 추후 개선 예정

### 프론트에서 쿠키에 접근하지 못하도록 HTTPONLY 설정 진행
기존에는 서버에서 헤더에 jwt 정보를 담아 응답했다.
프론트에서는 이 정보를 가지고 쿠키에 토큰을 저장한다.
백엔드와 프론트간 jwt 유효 시간이 다른 문제가 발생했다.
또한 클라이언트가 토큰에 접근할 수 있기 때문에 보안 취약점이 발생했다.

따라서 보안을 위해 프론트에서 쿠키에 접근하지 못하도록 설정하기로 결정했다.

서버에서는 httpOnly 설정된 쿠키를 응답에 담아준다.
프론트에서는 해당 정보에 접근하지 못하고, 요청시에 포함할건지만 결정할 수 있다.
프론트에서 로그인 후에 /me 경로로 로그인한 유저의 정보를 요청하도록 설정했다.

## 리팩토링
> 내용을 다 담기에는 가독성이 좋지 않아 링크로 대체 합니다.
### [1. 유저 프로필 사진 저장 로직 개선](https://velog.io/@regular_jk_kim/유저-프로필-사진-저장-로직-개선)
### [2. 평문처럼 읽히는 코드를 만들고 싶다!](https://velog.io/@regular_jk_kim/평문처럼-읽히는-코드를-만들고-싶다)
### [3. SALT 추가 로직 테스트하기](https://velog.io/@regular_jk_kim/SALT-추가-로직-테스트하기)
### [4. Vue.js Component와 View 분리하기](https://velog.io/@regular_jk_kim/Vue.js-Component와-View-분리하기)
### [5. HttpOnly 쿠키로 jwt 발급, 사용하기](https://velog.io/@regular_jk_kim/HttpOnly-쿠키로-jwt-발급-사용하기)
