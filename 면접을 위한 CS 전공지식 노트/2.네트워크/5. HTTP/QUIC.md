## QUIC
- 전송 계층 프로토콜
- UDP 기반
- QUIC = HTTP/2 + TLS + UDP

QUIC은 UDP를 전송 프로토콜로 채택하여 TCP보다 대기 시간이 짧고 처리량이 높다.   
<br/>

|TCP||UDP|
|-----|-----|-----|
|연결형 서비스|연결 방식|비연결형 서비스|
|가상 회선 방식|패킷 교환|데이터 그램 방식|
|보장함|전송 순서 보장|보장하지 않음|
|높음|신뢰성|낮음|
|느림|전송 속도|빠름|  

### HTTP/3은 왜 UDP를 선택했을까?
#### TCP Header
![image](https://github.com/hong-gp/study/assets/127091213/abe42257-896b-436e-b2bd-109c2a68c52c)

신뢰성을 확보하기 위해 다양한 기능으로 채워져있다. 그만큼 지연을 줄이기 힘든 구조  

#### UDP Header
![image](https://github.com/hong-gp/study/assets/127091213/7551ce3a-b81b-4e2b-a841-089fcc288a63)

- 데이터 전송에 집중한 설계
- 별도의 기능이 없다.
    - 원하는 기능을 구현할 여지가 많다.
    - TCP의 지연을 줄이며 신뢰성 확보 가능

<br/>

QUIC이 데이터 순서를 보장하는 방법은 스트림의 우선순위다. 먼저 전송해야하는 데이터는 우선순위를 높게 설정해서 먼저 전송한다. 
패킷마다 일련번호가 부여되며, 데이터를 올바른 순서로 형성할 수 있다. 

<br/>

QUIC이 출시되기 전 TCP가 HTTP에서 데이터를 전송하기 위한 기본 프로토콜로 사용되었다. 
TCP에는 문제점이 몇 가지 있다.  
- 핸드셰이크 지연
    - TCP 3-way handshake
    - TLS handshake
    - 총 두 번의 RTT 발생
- TCP 스트림의 HOL Blocking
    - 스트림의 데이터가 손실되면 손실된 데이터가 재전송될 때까지 그 뒤에 있는 다른 스트림의 데이터가 차단된다.

![image](https://github.com/hong-gp/study/assets/127091213/c4b1a733-aeb9-4fbd-89ca-c7e9da39e7a1)

<br/>

### QUIC이 필요한 이유
#### 빠른 핸드 셰이크 및 연결 설정
QUIC은 TCP의 일반적인 핸드셰이크와 TLS 핸드셰이크를 결합했다. 한 번의 왕복으로 연결이 가능하다.  
이전에 연결된 클라이언트는 캐시된 정보를 사용해서 0-RTT로 줄일 수 있다.  
![image](https://github.com/hong-gp/study/assets/127091213/5af507d0-36a7-429e-be11-c3fd83c8afbc)

![image](https://github.com/hong-gp/study/assets/127091213/8e7d9dbd-2336-464b-938d-3afa80b8fc4e)

<br/>

#### 멀티플렉싱 개선
QUIC은 독립 스트림을 사용해서 하나의 스트림에서 패킷이 손실되더라도 다른 스트림은 영향이 가지 않는다.  
![image](https://github.com/hong-gp/study/assets/127091213/07533ae0-cb59-4a77-9453-3c2270276514)

<br/>

#### 암호화된 패킷
QUIC은 패킷 헤더와 Handshake 과정에서 공개되는 일부 정보를 제외하고는 암호화된 채로 이루어진다. 
<br/>

---
### 참고
- https://medium.com/rate-labs/quic-%ED%94%84%EB%A1%9C%ED%86%A0%EC%BD%9C-%EA%B5%AC%EA%B8%80-%EB%98%90-%EB%84%88%EC%95%BC-932befde91a1
- https://www.cdnetworks.com/ko/media-delivery-blog/what-is-quic/
- https://www.youtube.com/watch?v=xcrjamphIp4
