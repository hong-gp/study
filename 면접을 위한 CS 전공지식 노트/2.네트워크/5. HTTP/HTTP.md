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

### Domain sharding
같은 서버에서 도메인 명을 다르게 해서 리소스를 저장하는 형태  
반대로 브라우저에서 요청하게 되면 나눠진 도메인에서 리소스를 병렬적으로 다시 보내주는 방식  
![image](https://github.com/hong-gp/study/assets/127091213/9aa79f12-5f39-41c8-9889-42c797d7aeba)  

도메인마다 커넥션을 맺고 끊음  
상당한 시간과 대역폭 소모  
연결할 수 있는 커넥션 수의 제한  
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
웹 페이지는 몇 년에 걸쳐 더 복잡해졌다. 훨씬 더 많은 HTTP 요청을 통해 많은 데이터가 전송되었고 이로 인해
HTTP/1.1 연결에 복잡성과 오버헤드가 많이 발생했다. 이를 해결하기 위해 Google은 SPDY 프로토콜을 구현했다. 
중복 데이터 전송 문제를 해결한 SPDY를 기반으로 HTTP/2 프로토콜이 등장하게 되었다.  
<br/>

- 바이너리 프레이밍 계층을 사용한다.
    - HTTP 메시지가 캡슐화되어 클라이언트와 서버 사이에 전송되는 방식을 규정한다.
    - 파싱, 전송 속도 증가, 오류 발생 가능성 감소
- 멑티플렉싱
    - 동일한 연결을 통해 병렬 요청을 수행할 수 있다.
    - 여러개의 스트림을 사용하여 하나의 스트림의 손실이 있더라도 나머지 스트림은 정상 동작
- 헤더 압축
    - HPACK 압축방식으로 헤더를 압축
    - 페이지 로드 시간 감소
- 서버 푸시
    - 서버가 클라이언트에게 리소스를 미리 예측하여 푸시할 수 있는 기능
    - 리소스를 요청할 것으로 예상되는 상황에서 RTT 수를 하나 이상 줄여 빠른 응답 제공

<br/>

### Multiplexing
#### Frame
- HTTP/2에서 통신의 최소 단위
- 최소 하나의 프레임 헤더
- 바이너리로 인코딩

#### Message
- 다수의 프레임
- 요청 응답의 단위

#### Stream
- 양방향 통신을 통해 전달되는 한 개 이상의 메시지

스트림을 여러개를 통해서 병렬적인 처리를 하게 되면 순차적인 개념이 깨진다.  
<br/>

### Header Compression
#### HPACK 압축방식
헤더 인덱싱을 하고 인코딩을 하는 방식  
이전에 전달된 중복된 헤더에 대해서는 생략을 하고 새로 들어온 헤더에 대해서는 인코딩한다.
<br/>

### Server Push
#### HTTP/1.x
1. HTML 요청
2. HTML 태그 파싱
3. 필요한 리소스 재요청(css, js, ...)
4. 웹페이지 완성

#### HTTP/2.0
1. HTML 요청
2. 웹페이지 완성

![image](https://github.com/hong-gp/study/assets/127091213/4b21f3b9-f9a2-4b5f-b6b1-ba6c9d71c577)

<br/>

### 한계
TCP 3-핸드 셰이크, TLS 핸드 셰이킹이 동시에 일어나게 되어 성능적인 문제가 있다.

## HTTPS
HTTP에서 SSL을 사용한 프로토콜, SSL은 서버와 브라우저 사이에 안전하게 암호화된 연결을 만들 수 있게 도와주고, 
서버와 브라우저가 민감한 정보를 주고받을 때 해당 정보가 도난당하는 것을 막아준다.  

HTTP 헤더는 암호화 되지 않고, HTTP 메시지 바디를 암호화하는 것이다.  
<br/>

### SSL/TLS
웹 서버와 웹 브라우저간의 보안을 위해 만든 프로토콜  
공개키/개인키, 대칭키 기반으로 사용함  

> SSL가 업그레이드 되어서 TLS라는 명칭으로 바뀐것이고, 엄밀히 따지면 다른 것이지만, 
> 의미상으로는 같다고 보면된다.

<br/>

### TLS Handshake
TLS 1.3에서는 암호화 방식을 각각 합의한다. 그리고 키 교환 방식도 ECDHE 혹은 DH만을 지원한다. 따라서 
클라이언트는 시작할 때부터 "ClientHello"를 보낼 때 지원하는 키 교환 방식에 해당하는 모든 방법을 
TLS 핸드셰이크 확장기능인 "key-share"와 함께 보낸다.  

![image](https://github.com/hong-gp/study/assets/127091213/130054d2-b6b1-473e-aafb-9afa7d2b01c1)

클라이언트가 데이터를 보낼때, 사이퍼 슈트도 전달한다. 서버는 전달받은 사이퍼 슈트의 리스트에서 
호환되는 알고리즘을 선택하고 사용하여 통신이 수행된다.  
<br/>

### 사이퍼 슈트(Cipher Suite)
암호화 협상을 위한 알고리즘을 무엇을 쓸 것인지 나열한 것  

![image](https://github.com/hong-gp/study/assets/127091213/e6ecc7d2-8110-4b7a-83c9-09683f67962e)  

`Bulk Encryption Algorithm`은 AEAD Cipher를 나타내며, 이는 데이터 암호화 알고리즘이다.  

<br/>

### 암호화 알고리즘
암호화 알고리즘에는 대칭키와 공개키(비대칭키)라는 두 가지 유형의 알고리즘이 있다.  

#### 대칭키
동일한 키로 암호화, 복호화를 수행하는 방법  

- 누구든지 암호화에 이용된 키를 가지고 있으면 해당 데이터를 쉽게 복호화 할 수 있다.
- 키를 배송할 때 보안 문제가 있음.

<br/>

#### 공개키
서로 다른 키로 암호화, 복호화를 하는 방법 (비대칭키)  
데이터 암호화시에는 공개키를 사용하고, 복호화시에는 개인키를 사용한다.  

- 개인키를 가지고 복호화를 하기때문에 공개키를 누구나 가져도 쉽게 복호화할 수 없다.
- 암호화 연산 시간이 대칭키보다 더 소요되기 때문에 비용이 크다.

<br/>

TLS는 대칭키와 공개키를 키 교환 알고리즘을 통해 적절히 혼합하여 사용한다.  

### 키 교환 알고리즘
키 교환 알고리즘은 Diffie-Hellman Ephemeral(DHE), Diffi Curve Diffie-Hellman Ephermeral(ECDHE)를 사용한다.  
둘 다 Diffie-Hellman 방식을 근간으로 만들어졌다. 

![image](https://github.com/hong-gp/study/assets/127091213/0bc75e80-7bc5-45fb-8b60-54310bf99555)

처음에 공개 값을 공유하고 각자의 비밀 값과 혼합한 후 혼합 값을 공유한다. 
그 다음 각자의 비밀 값과 다시 혼합해서 공통의 암호키인 PSK가 생성된다.  

<br/>

### TLS 통신 과정
TLS은 공개키 방식으로 대칭키를 전달하고, 대칭키를 활용해서 암호화, 복호화를 하고 서버와 브라우저가 통신한다.

1. A가 B에게 접속 요청
2. B가 A에게 자신의 공개키를 전송
3. A는 자신의 대칭키를 B에게 받은 공개키로 암호화하고 B에게 전송한다.
4. B는 암호화된 대칭키를 자신의 개인키로 복호화해서 A의 대칭키를 얻어낸다.
5. 이렇게 얻은 대칭키를 통해서 A와 B는 서로 안전하게 통신할 수 있다.  

<br/>

### CA 발급 과정
인증 메커니즘은 CA(Certificate Authorities)에서 발급한 인증서를 기반으로 이루어진다. 
CA에서 발급한 인증서는 안전한 연결을 시작하는 데 있어 필요한 공개키를 클라이언트에게 제공하고 
사용자가 접속한 서버가 신뢰할 수 있는 서버임을 보장한다.  

1. 사이트는 사이트 인증서가 필요하다. 사이트에서 인증기관에게 사이트 정보과 공개키를 전달한다. 
2. 인증기관은 전달받은 데이터를 검증해서 인증기관의 개인키로 서명해서 사이트 인증서를 사이트에게 전달한다.
3. 인증기관은 사용자에게 자신의 공개키를 전달한다.
4. 사용자가 인증기관에게 전달받은 공개키는 사용자 브라우저에 내장된다. 
5. 사용자가 사이트에 접속 요청하면 사이트는 사이트 인증서를 사용자에게 전달한다. 
6. 사용자는 브라우저에 내장되어있는 인증기관 공개키를 이용해서 사이트 인증서를 복호화한다. 
7. 사이트 정보와 사이트 공개키를 얻어내고, 사이트 공개키로 사용자 대칭키를 암호화 한다. 
8. 암호화한 대칭키를 사이트에게 전달하면 사이트의 개인키로 해독해서 대칭키를 얻어낸다.
9. 이 대칭키를 통해 SSL 통신을 할 수 있다.

<br/>

### 해싱 알고리즘
원본 데이터마다 일정한 결과값을 도출하며 원본 데이터가 변경되면 결과값 또한 달라지는 특성을 지닌 알고리즘  
해싱 알고리즘 중 하나인 SHA-256 알고리즘을 통해 알아보자.  

SHA-256 알고리즘을 평문에 적용했을 때 알아보기 어려운 해시값이 나타나는 것을 볼 수 있다.  

|INPUT|OUTPUT|
|-----|-----|
|객체지향의 사실과 오해|46d2fbf8a0bdfc39e3c2f84941c61086ab588bdf9e9fed90c33a97f7efa7092a|  

송신자는 원본 데이터와 해시 알고리즘이 적용된 데이터를 함께 전송하고 수신자는 약속한 해시 알고리즘을 이용해 
원본 데이터에 적용해 본다. 그 결과가 같다면 변조되지 않은 값이라는 것을 알 수 있다.  

<br/>

---
### 참고
- https://developer.mozilla.org/ko/docs/Web/HTTP/Basics_of_HTTP/Evolution_of_HTTP
- https://bbo-blog.tistory.com/87
- https://velog.io/@minu/HTTP1.0-HTTP1.1-HTTP2-and-QUIC
- https://simgee.tistory.com/28
- https://blog.cloudflare.com/http-2-for-web-developers
- 
- https://www.youtube.com/watch?v=wPdH7lJ8jf0
- https://sectigostore.com/blog/what-is-an-ssl-tls-cipher-suite/
- https://aws-hyoh.tistory.com/47
