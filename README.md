# ✈️ Tricount 클론코딩 프로젝트

이 프로젝트는 Tricount 정산 애플리케이션의 서버 파트를 클론코딩하는 PBL 미션입니다.
<br>
사용자들이 서로 간의 경비를 정산하고, 지출 내역을 공유할 수 있는 기능을 구현합니다.

## 📊 주요 기능

### 회원가입 (SignUp API)
- 유저 정보: 기본 키, 아이디, 비밀번호, 이름(닉네임)

### 정산 (Settlement API)
- 한 유저는 여러 개의 정산을 만들 수 있습니다.
- 유저는 여러 정산에 참가할 수 있습니다.
- 특정 정산에 참가한 유저들만 해당 정산 내역을 열람할 수 있습니다.

### 지출 (Expense API)
- 지출 정보: 지출 ID, 지출 이름, 지출한 유저, 지출 금액, 지출 날짜
- 하나의 정산은 여러 개의 지출 정보를 포함할 수 있습니다.

### 정산 결과 (Balance API)
- 하나의 정산은 하나의 정산 결과를 가지고 있습니다.
- 정산 결과는 정산에 참여한 유저들 간의 송금 금액을 보여줍니다.

```json
[
    {
        "senderUserNo": 2,
        "senderUserName": "유저2",
        "sendAmount": 60000,
        "receiverUserNo": 1, 
        "receiverUserName": "유저1"
    }, 
    {
        "senderUserNo": 3,
        "senderUserName": "유저3",
        "sendAmount": 10000,
        "receiverUserNo": 4, 
        "receiverUserName": "유저4"
    }
]
```

## 🚀 구현 요구사항

### 공통 API
- **스프링 부트**를 사용하여 AutoConfiguration을 적용합니다.
- CRUD 중 **CREATE, READ, DELETE**만 구현하며, UPDATE 기능은 제외합니다.
- 애플리케이션은 **executable jar**로 패키징되어야 하며, `java -jar`로 실행 가능해야 합니다.
- **API**만 구현하며, 프론트엔드 화면 구성은 필요하지 않습니다.
- **JSON** 통신을 사용합니다.

### 데이터베이스
- 데이터는 **H2 데이터베이스**를 사용하여 저장합니다.
- **JdbcTemplate**을 사용합니다.

### 기능 API
- 회원가입 기능은 스프링 MVC 2편 커리큘럼 중 **[섹션6. 로그인 처리1 - 쿠키, 세션]**, **[섹션7. 로그인 처리2 - 필터, 인터셉터]** 를 활용합니다.
- 금액 처리는 **BigDecimal 클래스**를 사용하며, 1원 단위에서 반올림합니다.


## 🗂️ 필수 API 목록
<img width="300" alt="스크린샷 2024-03-18 오후 6 38 31" src="https://github.com/solmoonkang/spring-tricount/assets/109902582/5847d243-2fed-464b-8edd-d679451dd29a">

## 📄 결과물 요구사항

- 기능을 구현한 executable jar 파일
- 결과물은 구글 드라이브에 업로드 후 링크를 공유합니다.

<img width="965" alt="스크린샷 2024-03-19 오전 12 56 27" src="https://github.com/solmoonkang/spring-tricount/assets/109902582/7ee36d94-3232-4c4e-bc99-32eca910c871">
