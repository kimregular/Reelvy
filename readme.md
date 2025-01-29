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

서버에서 발급받은 jwt를 쿠키에 저장한 후 요청할 때마다 쿠키에 저장된 토큰을 헤더에 넣기로 결정

```ts
// useAuthStore.ts
import {defineStore} from "pinia";
import {setCookie, getCookie, deleteCookie} from "@/utils/cookieUtil.ts";

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
    const response = await axios.get(`${BASE_URL}/v1/user/getInfo`, {
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

### LoginFilter 동작하지 않는 문제

```java

@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	return http // 설명을 위해 불필요한 설정 코드는 제외함
			.addFilterBefore(jwtAuthenticationFilter(), JwtLoginFilter.class)
			.addFilterAt(jwtLoginFilter(authenticationManager(authenticationConfiguration)), UsernamePasswordAuthenticationFilter.class).build();
}
```

#### 필터의 동작 순서

위 코드를 보면 필터는 아래와 같이 동작한다.

1. `jwtAuthenticationFilter` 먼저 jwt 확인
2. `jwtLoginFilter` 위 필터에서 확인이 안 되면 로그인 필터 동작

#### 문제의 코드

```java
// JwtAuthenticationFilter.class

@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

	String token = jwtUtil.resolveToken(request);

	if (jwtUtil.isValidToken(token)) {
		Authentication authentication = jwtUtil.getAuthentication(token);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	} else {
		log.info("Invalid or Missing JWT Token");
	}

	filterChain.doFilter(request, response);
}
```

JwtAuthenticationFilter 가 먼저 동작하여 토큰을 확인한다.
로그인을 하지 않았으므로 isValidToken에서 false가 출력되길 기대한다.

```java
// JwtUtil.class
public boolean isValidToken(String token) {
	try {
		getClaims(token);
		return true;
	} catch (ExpiredJwtException e) {
		throw new ExpiredJwtException(null, null, "Expired JWT token: 만료된 JWT token 입니다.");
	} catch (MalformedJwtException e) { // MalformedJwtException을 명확하게 처리
		throw new MalformedJwtException("Malformed JWT token: 잘못된 형식의 JWT 토큰입니다.");
	} catch (SecurityException e) { // SecurityException을 따로 처리
		throw new SecurityException("Invalid JWT signature: 유효하지 않은 JWT 서명입니다.");
	} catch (UnsupportedJwtException e) {
		throw new UnsupportedJwtException("Unsupported JWT token: 지원되지 않는 JWT 토큰입니다.");
	} catch (Exception e) {
		throw new RuntimeException("Unexpected error occurred while validating JWT token: " + e.getMessage());
	}
}
```

isValidToken 에서는 true만 반환하고 그 외에는 모두 예외를 던진다.
따라서 JwtAuthenticationFilter에서 doFilter를 호출하지 못하고 로직 종료

#### 해결

```java
public boolean isValidToken(String token) {
	if (token == null || token.trim().isEmpty()) {
		log.info("Token is null or empty");
		return false; // false 출력을 안 하면 LoginFilter 동작 안 함
	}

	try {
		getClaims(token);
		return true;
	} catch (ExpiredJwtException e) {
		throw new ExpiredJwtException(null, null, "Expired JWT token: 만료된 JWT token 입니다.");
	} catch (MalformedJwtException e) { // MalformedJwtException을 명확하게 처리
		throw new MalformedJwtException("Malformed JWT token: 잘못된 형식의 JWT 토큰입니다.");
	} catch (SecurityException e) { // SecurityException을 따로 처리
		throw new SecurityException("Invalid JWT signature: 유효하지 않은 JWT 서명입니다.");
	} catch (UnsupportedJwtException e) {
		throw new UnsupportedJwtException("Unsupported JWT token: 지원되지 않는 JWT 토큰입니다.");
	} catch (Exception e) {
		throw new RuntimeException("Unexpected error occurred while validating JWT token: " + e.getMessage());
	}
}
```

토큰이 비어있을 경우에는 false를 반환하도록 로직 변경!