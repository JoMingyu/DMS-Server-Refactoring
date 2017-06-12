# DMS-Server-Refactoring
DMS 서버를 리팩토링하자

## 문제점들
### 프로젝트
#### 기존
사용하지 않는 import가 그대로 남아 있음

대부분의 문자열 간 연산에서 StringBuilder를 미사용

Maven 모듈 간 인식이 되지 않는 오류 존재

POM에 불필요한 dependency

### 어노테이션
#### 기존
@RouteRegistration(path = "", method = {HttpMethod.***})
#### 현재
@API()

@REST()

@Route(uri = "", method = HttpMethod.***}
+ API 문서 자동화

### 데이터베이스
#### 테이블
필요하지 않은 컬럼들이 일부 존재
#### 기존
쿼리문 전체를 받아 StringBuilder로 처리

Statement를 사용하여 SQL 인젝션에 위험한 상태


#### 현재
PreparedStatement를 사용하여 placeholder로 처리

### 계정 관련 클래스
#### 기존
메소드를 호출하는 구문이 하나밖에 없음에도 불구하고 과도한 클래스화가 많았음

사용하지 않는 메소드 일부 존재
#### 현재


## 변경하자
ALTER TABLE `account` CHANGE COLUMN `session_key` `session_id` VARCHAR(128) NULL DEFAULT NULL ;

ALTER TABLE `admin_account` CHANGE COLUMN `session_key` `session_id` VARCHAR(128) NULL DEFAULT NULL ;

ALTER TABLE `student_data` DROP COLUMN `status`;
