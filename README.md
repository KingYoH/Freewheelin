# Recruitment task

### 0. Auth 관련
 - name(unique)과 password로 유저정보 생성 및 로그인.
 - JWT 토큰 (Access-Token 헤더)
```json
# 예시
### 회원가입
POST {{host}}/auth/sign-up
Content-Type: application/json

{
  "name": "teacher3",
  "password": "1234",
  "memberType": "TEACHER"  // "TEACHER" , "STUDENT"
}

### 로그인(teacher1)
POST {{host}}/auth/sign-in
Content-Type: application/json

{
  "name": "teacher1",
  "password": "1234"
}
```

### 1. `GET`  문제 조회
- 선생님은 총 문제 수, 유형코드 리스트, 난이도, 문제 유형(주관식, 객관식, 전체)을 조건으로 문제를 조회합니다.
- 클라이언트에서 에러처리를 원활하게 하기 위하여 resultCode와 message를 추가하고 data에 body를 담았습니다.
```json
  REQUEST
    * Query Params
       # totalCount : 총문제 수
       # unitCodeList : 유형코드 리스트
       # level : 난이도 (LOW, MIDDLE, HIGH)
       # problemType: 주관식, 객관식, 전체 (ALL, SUBJECTIVE, SELECTION)
  
  RESPONSE
    ex)
    {
        "resultCode": "Success", // 성공여부 / "Error"
        "data : {
                  "problemList" : [
                              {
                                  "id" : 1,
                                  "answer" : "정답",
                                  "unitCode" : "유형코드",
                                  "level" : 1,
                                  "problemType": "SELECTION",
                              },
                              {
                                  "id" : 2,
                                  "answer" : "정답",
                                  "unitCode" : "유형코드",
                                  "level" : 1,
                                  "problemType": "SELECTION",
                              }
                          ...
                          ]
                  }
        "message": "ok" // 메세지( 성공이면 ok, 아니면 error Message )
    }
```
```json
  # 예시
  GET {{host}}/problems?totalCount=15
        &unitCodeList=uc1503,uc1506,uc1510,uc1513,uc1519,uc1520,uc1521,uc1523,uc1524,uc1526,uc1529&
        &level=LOW&&problemType=ALL
  Content-Type: application/json
  Access-Token: {{accessToken}}
```

### 2. `POST`  학습지 생성
- 선생님은 **1번에서 조회했던 문제 리스트**를 바탕으로 학습지를 생성합니다.

- REQUEST
  ex ) http://localhost:8080/piece
```json
REQUESTBODY
  {
    "pieceName" :  "학습지명"
    "problems" :  ["문제번호1","문제번호2",..]
  }

RESPONSEBODY
  {
    "resultCode": "Success", // 성공여부 / "Error"
    "data": {
      "pieceId": 1, // 학습지Id
      "pieceName": "peice1", // 학습지명
      "problemCount": 10  // 포함되는 문제 개수
    },
    "message": "ok" // 메세지( 성공이면 ok, 아니면 error Message )
  }
```
```json
# 예시
POST {{host}}/piece
Content-Type: application/json
Access-Token: {{accessToken}}

{
  "pieceName": "peice1",
  "problems": [1532,1536,1535,1530,1538,1534,1539,1212,1331,1213]
}

```



### 3. POST  학생에게 학습지 출제하기
- 선생님은 학생에게 **2번 문제에서 생성했던 학습지** **1개의 학습지**를 출제합니다.
```json
REQUEST
  * Path Param
    # pieceId : 제출할 학습지Id
  * Query Params
    # studentIds: 제출할 학생Id 리스트
        
RESPONSEBODY
{
    "resultCode": "Success", // 성공여부 / "Error"
    "data": {
        "sucess": [3, 4, 5], // 제출성공한 학생 ID
        "alreadySubmitted": [] // 이미 제출되어있었던 학생 ID
    },
    "message": "ok" // 메세지( 성공이면 ok, 아니면 error Message )
}
```
```json
# 예시
POST {{host}}/piece/1?studentIds=3,4,5
Access-Token: {{accessToken}}
```

### 4. `GET` 학습지의 문제 조회하기

- 학생은 자신에게 출제된 학습지의 문제 목록을 확인할 수 있습니다.
- 학습지 1개에 대한 문제목록을 확인하는 것입니다.
- 클라이언트는 이 api를 바탕으로 문제풀이 화면을 구현합니다.
```json
REQUEST
  * Query Params
    # pieceId: 조회할 학습지Id

RESPONSEBODY
  {
    "resultCode": "Success", // 성공여부 / "Error"
    "data": {
        "problems": [
            {
                "id": 1212, //문제Id
                "unitCode": "uc1513", // 문제 유형코드
                "unitCodeName": "세 개 이상 집합의 교집합", // 문제 유형 이름
                "level": 3,   // 문제 난이도
                "problemType": "SUBJECTIVE" // 문제 종류
            },
        ...
        ]
    },
    "message": "ok" // 메세지( 성공이면 ok, 아니면 error Message )
  }
```
```json
# 예시
GET {{host}}/piece/problems?pieceId=1
Access-Token: {{accessToken}}
```

### 5. `PUT` 채점하기
- 학생은 4번 문제에서 조회했던 문제들을 채점할 수 있습니다.
```json
REQUEST
{
  "answers": [
    {
      "problemId": 1212, //문제번호
      "answer" : "1"  // 제출할 정답
    },
    {
      "problemId": 1213,
      "answer" : "2"
    },
    ...
  ]
}

RESPONSEBODY
{
  "resultCode": "Success", // 성공여부 / "Error"
  "data": {
    "results": [
      {
        "problemId": 1212, // 문제번호
        "correct": true   // 정답 여부
      },
      {
        "problemId": 1213,
        "correct": false
      },
      ...
    ]
  },
  "message": "ok" // 메세지( 성공이면 ok, 아니면 error Message )
}
```
```json
# 예시
### 1212 맞추고, 1213 틀림)
PUT {{host}}/piece/problems?pieceId=1
Content-Type: application/json
Access-Token: {{accessToken}}

{
    "answers": [
        {
        "problemId": 1212,
        "answer" : "1"
        },
        {
        "problemId": 1213,
        "answer" : "2"
        }
    ]
}
```

### 6. `GET` 학습지 학습 통계 분석하기

- 선생님은 1개의 학습지에 대해 학생들의 **학습 통계**를 파악할 수 있습니다.
- 선생님은 자신이 만든 학습지에 대해 학생들의 학습 통계 데이터를 분석할 수 있습니다.
- 선생님은 조회한 1개의 학습지에 대해 아래의 정보들을 파악 할 수 있습니다.
    - 학습지 ID
    - 학습지 이름
    - 출제한 학생들의 목록
    - 학생들의 학습 데이터
        - 학생 개별의 학습지 정답률
    - 학습지의 문제별 정답률 (출제받은 학생들에 한에서)

```json
```json
REQUEST
{
  "answers": [
    {
      "problemId": 1212, //문제번호
      "answer" : "1"  // 제출할 정답
    },
    {
      "problemId": 1213,
      "answer" : "2"
    },
    ...
  ]
}

RESPONSEBODY
{
  "resultCode": "Success", // 성공여부 / "Error"
  "data": {
    "pieceId": 1,
    "pieceName": "peice1",
    "students": [
      {
        "id": 3, // 학생 Id
        "name": "student1", // 학생 이름
        "correctRate": 50.0 // 해당 학생의 학습지 정답률
      },
      ...
    ],
    "problems": [
      {
        "id": 1212,   // 문제 Id
        "unitCode": "uc1513", // 문제 유형
        "unitCodeName": "세 개 이상 집합의 교집합", // 문제 유형 이름
        "level": 3, // 문제 난이도
        "problemType": "SUBJECTIVE", // 문제 타입
        "correctRate": 100.0 // 학습지에서 문제 정답률
      },
      ...
    ]
  },
  "message": "ok" // 메세지( 성공이면 ok, 아니면 error Message )
}
```
```json
# 예시
GET {{host}}/piece/analyze?pieceId=1
Access-Token: {{accessToken}}

```

### ERD

![ERD](https://github.com/user-attachments/assets/82e93b42-1c25-4585-83de-8e8202ab4103)

### 논의
- 통계를 조회하기 편하게 하기 위해서, 테이블끼리 다대다 연결이 아니라 
중간 테이블을 만들어서 사용하였습니다. 불필요한 조인을 줄이고 빠른 통계를 낼 수 있었습니다.

...
### api 실행 테스트 
src/main/http-test/test.http

## 테스트코드 
 - 미완성
