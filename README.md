# DMS-Server-Refactoring
DMS 서버를 리팩토링하자

## 변경 사항들(기존 : 현재)
### Code
사용하지 않는 import가 그대로 남아 있음 : Organize import 기능 적극 사용

대부분의 문자열 간 연산에서 StringBuilder 미사용 : 모든 String 연산에 대한 StringBuilder 사용

Maven 모듈 간 인식이 되지 않는 오류 존재 : 패키지 구조를 크게 변경하며 싱글 모듈로 구현

POM에 불필요한 dependency(자동으로 처리되는 Compile Dependency가 굳이 추가되어 있음) : 꼭 필요한 dependency만 적용

메소드를 호출하는 구문이 하나밖에 없음에도 불구하고 클래스화되어 있음 : 호출하는 라우팅 클래스에 직접 구현

사용하지 않는 메소드가 일부 존재 : Call Hierarchy를 이용해 사용하지 않는 메소드 제거

API 문서화에 대한 시간 소비 : API와 REST 어노테이션을 통한 자동 문서화

Statement와 StringBuilder를 이용해 쿼리문 전체를 받아서 직접 처리(SQL 인젝션에 취약) : PreparedStatement를 사용하여 placeholder로 처리


## 변경하자
ALTER TABLE `account` CHANGE COLUMN `session_key` `session_id` VARCHAR(128) NULL DEFAULT NULL;

ALTER TABLE `account` DROP COLUMN `permission`;

ALTER TABLE `admin_account` CHANGE COLUMN `session_key` `session_id` VARCHAR(128) NULL DEFAULT NULL;

ALTER TABLE `student_data` DROP COLUMN `status`;

ALTER TABLE `extension_apply` CHANGE COLUMN `class` `no` INT(1) NOT NULL;

ALTER TABLE `extension_map` CHANGE COLUMN `room` `no` INT(1) NOT NULL;

ALTER TABLE `meal` DROP COLUMN `dinner_allergy`, DROP COLUMN `lunch_allergy`, DROP COLUMN `breakfast_allergy;

ALTER TABLE `plan` DROP COLUMN `month`, CHANGE COLUMN `year` `date` DATE NOT NULL, CHANGE COLUMN `data` `data` VARCHAR(100) NOT NULL, DROP PRIMARY KEY, ADD PRIMARY KEY (`date`);

ALTER TABLE `plan` RENAME TO `schedule`;


