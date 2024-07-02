# Spring Boot web-board project

- spring boot 와 Thymeleaf, vue 로 제작한 게시판 프로젝트입니다.
- 일반적인 게시판 가까웠던 이전의 제 프로젝트를 더 심도있게 제작해 보고 싶었기에 진행하였습니다.

### 개발 기간
    2024.04.10 - 2024.04.30

### 주요 기능

- 사용자와 관리자의 인증 과정 분리. 
  - 사용자는 jwt 토큰을 이용해 인증하고 관리자는 session을 이용해 인증을 진행 했습니다.
- 한 프로젝트 내에서 SSR 과 SPA 모두 사용
  - 사용자 페이지는 vue.js 를 이용해 frontend 를 구현 했습니다.
  - 관리자 페이지는 thymeleaf 를 이용해 frontend 를 구현 했습니다.
- 첨부 파일 업로드 및 다운로드, 게시글 작성과 삭제, 댓글 작성 및 삭제, 게시글 정렬 등 게시판에 필요한 기능 대부분을 구현 했습니다.
- aws lambda 와 aws api gateway, gitlab 을 통해 배포를 진행했습니다.
- session 인증의 경우 redis database 를 이용해 관리자 인증 정보를 cache 한 뒤 존재하지 않을 때만 로그인을 요청하게끔 구현하였습니다.

###  [사용자 페이지](https://web-board-service.vercel.app)

    id: bbdlssdwww0099
    password: qq21fdhre34!


### [관리자 페이지](https://xnp2tr132k.execute-api.ap-northeast-2.amazonaws.com/api-stage/auth/login)

    id: aDmiN32940184
    password: rOlE329019



### 주요 기능 간략 설명
<details>
    <summary>사용자 인증 과정</summary>
    <pre>
        /**
         * 사용자 인증을 진행하는 url 로 접근시 실행되는 filterChain
         * @param security httpSecurity
         * @return SecurityFilterChain
         * @throws Exception exception
         */
        @Bean
        @Order(1)
        public SecurityFilterChain jwtChain(HttpSecurity security) throws Exception {
            security.securityMatcher("/api/auth/**", "/api/main/**", "/api/board/**", 
                    "/api/notify", "/api/question/**", "/api/gallery/**", "/api/user/**")
                    .csrf(AbstractHttpConfigurer::disable)
                    .exceptionHandling((exception)->
                            exception
                                    .authenticationEntryPoint(entryPoint)
                                    .accessDeniedHandler(deniedHandler)
                    )
                    .authorizeHttpRequests((authorizeHttpRequests) -> {
                        authorizeHttpRequests
                                .requestMatchers("/api/auth/login/**", "/api/auth/signIn/**" ).permitAll()
                                .requestMatchers("/api/main/**", "/api/board/view/download/**").permitAll()
                                .requestMatchers( "/api/notify/**", "/api/board/**",  "/api/question/**", 
                                    "/api/gallery/**").permitAll();
                        authorizeHttpRequests.requestMatchers("/api/user/**").hasRole("USER");
                    })
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .addFilterBefore(new JwtFilter(jwtProvider, repository), 
                                    UsernamePasswordAuthenticationFilter.class);
            return security.build();
        }
    </pre>
    <pre>
        /**
         * jwt 토큰 검증.
         */
        @RequiredArgsConstructor
        public class JwtFilter extends OncePerRequestFilter {
            private final JwtProvider jwtProvider;
            private final SecurityContextRepository repository ;
            @Override
            protected void doFilterInternal(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain filterChain) throws ServletException, IOException {
                String token = jwtProvider.resolveToken(request);
                if(token != null && jwtProvider.isValid(token)){
                    Authentication authentication = jwtProvider.getAuthentication(token);
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    context.setAuthentication(authentication);
                    SecurityContextHolder.setContext(context); 
                    repository.saveContext(context, request, response);
                }
                filterChain.doFilter(request , response);
            }
        }
    </pre>
</details>
<details>
    <summary>관리자 인증 과정</summary>
    <pre> 
        /**
         * 관리자 인증을 진행하는 url 로 접근시 실행되는 filterChain
         * @param security httpSecurity
         * @return SecurityFilterChain
         * @throws Exception exception
         */
        @Bean
        @Order(2)
        public SecurityFilterChain sessionChain(HttpSecurity security) throws Exception{
            security.securityMatcher("/admin/**", "/api/admin/**", "/auth/**")
                    .securityContext(context -> context.requireExplicitSave(true))
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests((authorizeHttpRequests) -> {
                        authorizeHttpRequests
                                .requestMatchers("/auth/login/**").permitAll();
                        authorizeHttpRequests
                                .requestMatchers("/admin/**", "/api/admin/**").hasRole("ADMIN");
                    })
                    .sessionManagement(session ->
                            session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                                    .invalidSessionUrl("/auth/login")
                                    .sessionAuthenticationFailureHandler(sessionAuthFailed)
                                    .maximumSessions(1)
                                    .maxSessionsPreventsLogin(true)
                                    .expiredUrl("/auth/login"))
                    .addFilterAfter(new RedisCacheFilter(repository, cacheService), 
                                UsernamePasswordAuthenticationFilter.class)
                    .addFilterBefore(new SessionFilter(sessionRegistry(), repository) , 
                                UsernamePasswordAuthenticationFilter.class);
            return security.build();
        } 
    </pre>
    <pre>
        /**
         * 관리자 인증시 진행되는 security filter chain
         */
        @RequiredArgsConstructor
        public class SessionFilter extends OncePerRequestFilter {
            private final SessionRegistry sessionRegistry;
            private final SecurityContextRepository repository;
            @Override
            protected void doFilterInternal(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain filterChain) throws ServletException, IOException {
                // security context 에 등록된 sessionId 중 request 에 들어있는 sessionId 가 없으면 진행.
                if(!repository.containsContext(request)){
                    HttpSession session = request.getSession(false);
                    // session 이 생성되어 있지 않거나 session 에 아예 정보가 없으면 그냥 filter chain 을 진행.
                    if (session != null && session.getId() != null) {
                        String sessionId = session.getId();
                        SessionInformation info = sessionRegistry.getSessionInformation(sessionId);
                        // 정보가 유효하고 security context 에 등록되어 있지 않는 경우에만 추가적으로 등록
                        if (info != null && !info.isExpired()) {
                            UserDetails details = (UserDetails) info.getPrincipal();
                            Authentication auth =
                                    new UsernamePasswordAuthenticationToken(details, "", details.getAuthorities());
                            SecurityContext context = SecurityContextHolder.createEmptyContext();
                            context.setAuthentication(auth);
                            SecurityContextHolder.setContext(context);
                            repository.saveContext(context, request, response);
                        }
                    }
                }
                // 위의 과정을 진행하지 않은 경우 controller 에서 직접 등록한다.
                filterChain.doFilter(request, response);
            }
        } 
    </pre>
</details>
<details>
    <summary>전역 에러 처리</summary>
    <pre> 
        /**
         * 전역 예외 처리자
         */
        @RestControllerAdvice
        public class ErrorRestControllerAdvice {
            /**
             * 에러 처리자. 성공의 경우 별도의 내용이 필요하기 때문에 제공하지 않음.
             * @param err 에러 내용.
             * @return 에러 내용이 담긴 responseEntity.
             */
            @ExceptionHandler(CustomRuntimeException.class)
            public ResponseEntity<ErrorResponses> handlingError(CustomRuntimeException err){
                return setErrorResponse(err.errorCode);
            }
        } 
    </pre> 
</details>
<details>
      <summary>spring security 에 redis cache 를 확인하는 filter chaine 을 동록해 redis cache 를 사용할 수 있게 만들었습니다.</summary>
    <pre>
    /**
     * SecurityContextRepository 에 존재하지 않으며 HttpSession 이 실행 되지도 않은 경우 실행한다.
     * 즉, 로그인을 한 뒤에 별도의 호출이 없으면 aws lambda 가 꺼지게 되는데,
     * sessionId 가 있고 redis 에 저장된 authentication 이 expire 되지 않았을 경우,
     * 저장된 authentication 을 SecurityContextRepository 에 다시 저장 시킨다.
     */
    @RequiredArgsConstructor
    public class RedisCacheFilter  extends OncePerRequestFilter {
        private final SecurityContextRepository repository ;
        private final CacheService cacheService; 
        @Override
        protected void doFilterInternal(HttpServletRequest request, 
                                        HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            if(!repository.containsContext(request)){
                Cookie[] cookies = request.getCookies();
                Cookie redisCookie = null;
                for(Cookie cookie : cookies) {
                    if (cookie.getName() != null && cookie.getName().equals("redisKey")){
                        redisCookie = cookie;
                    }
                } 
                // header 에 sessionId 가 존재할 경우
                if (redisCookie != null) {
                    String redisValue = redisCookie.getValue();
                    // redis 에서 sessionId 로 authentication 을 가져온다.
                    Authentication authentication = cacheService.parseACache(redisValue);
                    // 만약 expired 되거나 아예 존재하지 않을 경우는 제외하고 진행한다.
                    if(authentication != null){
                        // context 를 새롭게 생성한 뒤 가져온 authentication 을 넣어 로그인 상태를 유지시킨다.
                        SecurityContext context = SecurityContextHolder.createEmptyContext();
                        context.setAuthentication(authentication);
                        repository.saveContext(context, request, response);
                        request.setAttribute("redisKey", redisValue); 
                    }
                }
            }
            else {
                String sessionId = request.getRequestedSessionId();
                Authentication authentication = cacheService.parseACache(sessionId);
                if(authentication != null){
                    request.setAttribute("redisKey", sessionId);
                    response.setHeader("redisKey", sessionId); 
                }
            }
            filterChain.doFilter(request, response);
        }
    } 
</pre> 
</details>

### 문제 및 문제 해결 과정

<details>
    <summary>aws lambda 사용시 session 인증 유지 제한</summary>

    aws lambda 는 일정 시간동안 요청이 없으면 컨테이너를 종료 합니다. session 은 서버를 종료하면 유지되지 않기에 
    redis 를 cache database 로 사용하여 이를 보완했습니다.

</details>

### 아키텍쳐

![web-board-project](https://github.com/redoundo/web_board_service/assets/96558064/35d8f002-e330-40c5-aebb-81d6de88f35e)


### 기술 스택
##### frontend
![Vue.js](https://img.shields.io/badge/vuejs-%2335495e.svg?style=for-the-badge&logo=vuedotjs&logoColor=%234FC08D)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-%23005C0F.svg?style=for-the-badge&logo=Thymeleaf&logoColor=white)

##### backend
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=Spring%20Boot&logoColor=white)
![Static Badge](https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=Spring%20Security&logoColor=white)


##### database
![Mysql](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)
![MyBatis](https://img.shields.io/badge/mybatis-D70010?style=for-the-badge&logo=mybatis&logoColor=black)
![Redis](https://img.shields.io/badge/Redis-FF4438?style=for-the-badge&logo=redis&logoColor=white)
![Amazon Rds](https://img.shields.io/badge/Amazon%20Rds-527FFF?style=for-the-badge&logo=Amazon%20Rds&logoColor=white)

##### infra
![Vercel](https://img.shields.io/badge/Vercel-000000?style=for-the-badge&logo=Vercel&logoColor=white)
![Aws Lambda](https://img.shields.io/badge/Aws%20Lambda-FF9900?style=for-the-badge&logo=awslambda&logoColor=white)
![Amazon Api Gateway](https://img.shields.io/badge/Amazon%20Api%20Gateway-FF4F8B?style=for-the-badge&logo=Amazon%20Api%20Gateway&logoColor=white)

##### ci / cd
![GitLab](https://img.shields.io/badge/GitLab-FC6D26?style=for-the-badge&logo=gitlab&logoColor=white)

