# schedule-s-pronunciation-is-sheh-joolg

스케줄 관리 API입니다.
+ 스케줄 관리
    + 생성
    + 삭제
    + 수정
    + 조회
+ 유저 관리
    + 생성
    + 삭제
    + 수정
    + 조회
 
의 기능을 지원합니다.

스케줄은 사용자에 종속됩니다.

스케줄 생성, 수정, 삭제 시 기존 존재하는 사용자의 id, password가 필요합니다.

---

## API 명세

| 기능 | Method | URL                                                                                                                                                         | Request | Response | 상태코드 |
| --- | --- |-------------------------------------------------------------------------------------------------------------------------------------------------------------| --- | --- | --- |
| 스케줄 생성 | POST | /schedule                                                                                                                                                   | {<br>"plan":"스케줄 내용",<br>"userId":유저 id,<br>"password":"유저 패스워드"<br>}| {<br>"id": 스케줄 id,<br>"userId": 유저 id,<br>"userName": "유저 이름",<br>"plan": "스케줄 내용",<br>"createdDate": "yyyy-MM-dd",<br>"editedDate": "yyyy-MM-dd"<br>}<br><br>단건 응답|201: Created|
| 스케줄 검색 | GET | /schedule?<br>id=스케줄 id & <br>plan=스케줄 내용 & <br>userId=유저 id & <br>createdDate=yyyy-MM-dd & <br>editedDate=yyyy-MM-dd & <br>pageIndex=페이지번호<br> <br>파라미터는 생략 가능 | 요청 파라미터 | [<br>{<br>"id": 스케줄 id,<br>"userId": 유저 id,<br>"userName": "유저 이름",<br>"plan": "스케줄 내용",<br>"createdDate": "yyyy-MM-dd",<br>"editedDate": "yyyy-MM-dd"<br>}<br>] <br><br>다수 응답| 302: Found|
| 스케줄 수정 | PATCH | /schedule/{id}                                                                                                                                              | {<br>"plan":"수정할 스케줄 내용",<br>"userId":수정할 유저 id,<br>"password":"기존 유저 패스워드"<br>}<br><br>수정하지 않을 항목은 생략 가능| {<br>"id": 스케줄 id,<br>"userId": 유저 id,<br>"userName": "유저 이름",<br>"plan": "스케줄 내용",<br>"createdDate": "yyyy-MM-dd",<br>"editedDate": "yyyy-MM-dd"<br>}<br><br>단건 응답 | 200: OK|
| 스케줄 삭제 | DELETE | /schedule/{id}                                                                                                                                              | {<br>"password": "유저 패스워드"<br>}|-|200: OK|
| 유저 생성 | POST | /schedule/user                                                                                                                                              | {<br>"userName":"유저이름",<br>"userMail":"유저 이메일",<br>"password":"패스워드"<br>} | {<br>"userId":유저 Id,<br>"userName":"유저이름",<br>"userMail":"유저 이메일",<br>"registedDate":"등록 일자"<br>"editedDate":"수정 일자"<br>} | 201: Created|
| 유저 검색 | GET | /schedule/user?<br>userId=유저 Id & <br>userName=유저 이름 & <br>pageIndex=페이지번호<br> <br>파라미터는 생략 가능                                                              | 요청 파라미터 | [<br>{<br>"userId": 1,<br>"userName": "김",<br>"userMail": null,<br>"registedDate": "2025-03-08",<br> "editedDate": "2025-03-21"<br>}<br>] <br><br> 다수 응답| 302: Found |
| 유저 수정 | PATCH | /schedule/user/{id}                                                                                                                                         | {<br>"userName":"유저이름",<br>"userMail":"유저 이메일",<br>"password":"패스워드"<br>} <br><br>수정하지 않을 항목은 생략 가능 | {<br>"userId":유저 Id,<br>"userName":"유저이름",<br>"userMail":"유저 이메일",<br>"registedDate":"등록 일자"<br>"editedDate":"수정 일자"<br>} | 200: OK|
| 유저 삭제 | DELETE | /schedule/user/{id}                                                                                                                                         | {<br>"password": "유저 패스워드"<br>}|-|200: OK|



## ERD
![image](https://github.com/user-attachments/assets/363a572c-a856-4577-9f21-27fc794e94a0)

---

## 기술 스택
+ Spring boot
+ Thymeleaf
+ Java
+ Github
+ MySQL
