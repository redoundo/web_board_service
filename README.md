# spring boot web-board project
tomcat 과 jsp 로 제작한 게시판 프로젝트입니다.

## 개발 기간
2024.01.20 - 2024.01.27


디시인사이드와 같은 형식으로 입력한 비밀번호가 동일할 경우에만 변경할 수 있도록 만들었습니다.



## 개발 환경
jdk 11 , docker , tomcat 10 , jsp

### get 디폴트 요청
 - servlet 의 doGet 요청과 jsp 자체의 요청은 동일하지 않다. 
 - Thymeleaf와 Spring Boot를 사용할 때는 Spring Boot가 자동으로 GET 요청을 처리하고, 컨트롤러가 데이터를 준비하여 템플릿을 렌더링하는 방식으로 동작합니다. 반면, JSP와 서블릿을 사용할 때는 개발자가 직접 요청을 관리하고, 필요한 데이터를 설정하여 JSP 파일로 전달해야 합니다. 이러한 차이로 인해, Spring Boot와 Thymeleaf를 사용할 때는 URL 이동 시 자동으로 GET 요청이 발생하여 컨트롤러가 데이터를 준비하는 반면, JSP와 서블릿을 사용할 때는 직접 요청을 처리해야 합니다.
### jquery 무한 로딩
 - 