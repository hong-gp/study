HTTP는 초기에 서버로 부터 정보를 얻는 GET 방식만 사용 가능했다. 인터넷의 발전을 통해
HTTP도 발전하게 되었다.  
<br/>

## HTTP 1.0
- 각 요청안에 버전 정보가 포함되어 전송되었다.
- 상태 코드 또한 응답의 시작 부분에 붙어 전송되었다.
    - 브라우저가 요청에 대한 성공과 실패를 알 수 있고 그 결과에 대한 동작을 할 수 있게 되었다.
- HTTP 헤더 개념은 요청과 응답 둘 다 도입되어, 메타데이터 전송이 가능해졌고, 프로토콜이 유연하고 확장성이 높아졌다.
- `Content-Type` 덕분에, 일반 HTML 파일들 외에 다른 문서들을 전송할 수 있다. (json 등)

<br/>

### HTTP 요청
```HTTP
GET /mypage.html HTTP/1.0
User-Agent: NCSA_Mosaic/2.0 (Windows 3.1)
```
- `GET /mypage.html HTTP/1.0` : 클라이언트가 서버에서 "/mypage.html"이라는 페이지 요청  
- `User-Agent: NCSA_Mosaic/2.0 (Windows 3.1)` :
클라이언트가 서버에게 자신의 사용자 에이전트를 알리는 헤더  
<br/>

### HTTP 응답
```HTTP
200 OK
Date: Tue, 15 Nov 1994 08:12:31 GMT
Server: CERN/3.0 libwww/2.17
Content-Type: text/html

<HTML>
A page with an image
  <IMG SRC="/myimage.gif">
</HTML>
```
- `200 OK` : 상태 코드  
- `Date: Tue, 15 Nov 1994 08:12:31 GMT` : 응답이 생성된 날짜와 시간
- `Server: CERN/3.0 libwww/2.17` : 서버의 소프트웨어 및 버전 정보
- `Content-Type: text/html` : 응답 본문의 유형

### 단점
- 하나의 연결당 하나의 요청을 처리 -> 매번 새로운 연결로 성능 저하 및 서버 부하 비용 증가
- 하나의 데이터를 받을 때 마다 서버 측에서 연결을 끊음
- 요청마다 TCP의 3-웨이 핸드셰이크를 열어야 함
- RTT(Round Trip Time) 증가
    - 패킷이 목적지에 도달하고 나서 다시 출발지로 돌아오기 까지 걸리는 시간

<br/>

### RTT 증가를 해결하기 위한 방법
- 이미지 스플리팅
    - 이미지가 합쳐있는 하나의 이미지를 다운로드 받고 표기하는 방법
- 코드 압축
    - 개행 문자, 빈 칸을 없애서 코드의 크기를 최소화
- 이미지 Base64 인코등
    - 이미지 파일을 64진법으로 이루어진 문자열로 인코딩 하는 방법

<br/>

## HTTP 1.1
HTTP의 첫 번째 표준화 버전  
HTTP/1.1은 모호함을 명확하게 하고 많은 개선 사항들을 도입했다.  
<br/>

- 지속 연결(Persistent Connection) : 지정한 timeout 동안 커넥션을 닫지 않음
- 파이프라이닝을 추가하여, 첫 번째 요청에 대한 응답이 완전히 전송되기 전에 두 번째 요청 전송을 가능케하여, 통신 지연 시간이 단축되었다.
- 청크 인코딩 지원
- 추가적인 캐시 제어 메커니즘 도입
- 언어, 인코딩, 타입을 포함한 컨텐츠 협상이 도입되어, 클라이언트와 서버가 가장 적합한 컨텐츠를 교환할 수 있음
- Host 헤더 덕분에, 동일 IP 주소에 다른 도메인을 호스트할 수 있음

<br/>

### Persistent Connection
연결을 끊지않고 지속적으로 유지하여 불필요한 Handshake를 줄여 성능 개선  

![image](https://github.com/hong-gp/study/assets/127091213/89f64c27-90a4-45bc-9a61-5e388838d68f)  
<br/>

### Pipelining
여러개의 요청을 보낼 때, 처음 요청이 응답될 때까지 기다리지 않고 순차적인 여러 요청을 연속적으로 보내는 것  

![image](https://github.com/hong-gp/study/assets/127091213/3185093c-7886-4b98-9b54-705ded4b3b64)
<br/>

### 단점
- HOL Blocking(Head Of Line Blocking)
    - 우선순위로 들어온 요청의 응답 시간이 길어지면 뒤에 있는 요청의 응답 시간도 길어진다.
- 무거운 헤더 구조
    - HTTP/1.1의 헤더에는 쿠키 등의 많은 메타데이터가 들어있어 전송하려는 값보다 헤더값이 큰 경우가 많다.
    - 연속된 요청 데이터가 중복된 헤더값을 가지고 있는 경우가 많다.
<br/>

### 요청
```HTTP
GET /en-US/docs/Glossary/Simple_header HTTP/1.1
Host: developer.mozilla.org
User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:50.0) Gecko/20100101 Firefox/50.0
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
Accept-Language: en-US,en;q=0.5
Accept-Encoding: gzip, deflate, br
Referer: https://developer.mozilla.org/en-US/docs/Glossary/Simple_header
```
- `Accept` : 클라이언트가 받을 수 있는 콘텐츠 타입
- `Accept-Language` : 클라이언트가 선호하는 언어 지정
- `Accept-Encoding` : 클라이언트가 지원하는 콘텐츠 인코딩
- `Referer` : 요청을 보내기 전에 방문한 페이지의 URL

<br/>

### 응답
```HTTP
200 OK
Connection: Keep-Alive
Content-Encoding: gzip
Content-Type: text/html; charset=utf-8
Date: Wed, 20 Jul 2016 10:55:30 GMT
Etag: "547fa7e369ef56031dd3bff2ace9fc0832eb251a"
Keep-Alive: timeout=5, max=1000
Last-Modified: Tue, 19 Jul 2016 00:59:33 GMT
Server: Apache
Transfer-Encoding: chunked
Vary: Cookie, Accept-Encoding
```
- `Connection` : 클라이언트와 서버간의 연결 상태
- `Content-Encoding` : 응답 본문의 압축 방식
- `Etag` : 리소스의 현재 상태
- `Keep-Alive` : Keep-Alive 연결의 설정
- `Last-Modified` : 리소스의 마지막 수정 날짜, 시간
- `Transfer-Encoding` : 응답 본문의 전송 방식
- `Vary` : 캐시와 프록시 서버에게 응답의 변이를 결정하는 데 사용되는 헤더

<br/>

## HTTP 2.0


<br/>

---
### 참고
- https://developer.mozilla.org/ko/docs/Web/HTTP/Basics_of_HTTP/Evolution_of_HTTP
- https://bbo-blog.tistory.com/87
- https://velog.io/@minu/HTTP1.0-HTTP1.1-HTTP2-and-QUIC
- https://simgee.tistory.com/28
