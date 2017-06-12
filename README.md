# DMS-Server-Refactoring
DMS 서버를 리팩토링하자

## 변경하자
ALTER TABLE `account` CHANGE COLUMN `session_key` `session_id` VARCHAR(128) NULL DEFAULT NULL ;
ALTER TABLE `admin_account` CHANGE COLUMN `session_key` `session_id` VARCHAR(128) NULL DEFAULT NULL ;
ALTER TABLE `student_data` DROP COLUMN `status`;
