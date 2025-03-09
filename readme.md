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
<!-- TOC -->

## 사용자 동영상 공유 플랫폼

- 백엔드 : 자바 스프링
- 프론트엔드 : 뷰.js

## Trouble Shootings

### Intellij Http Client 응답 변수 설정하기

```http request
### Post Login Success
POST http://localhost:8080/api/v1/users/login
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
GET http://localhost:8080/api/v1/users/getInfo
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

파일을 json 객체로 전달하면 서버가 이를 파일로 처리하지 않고, 텍스트 데이터로 처리한다.

파일을 업로드 할 때에는 formData를 사용해야한다.
`FormData` 객체는 `multipart/form-data` 형식으로 데이터를 전송할 수 있도록 도와준다.

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