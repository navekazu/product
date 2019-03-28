SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema studyup_sys
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `studyup_sys` ;

CREATE SCHEMA IF NOT EXISTS `studyup_sys`
DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `studyup_sys` ;

SET SQL_SAFE_UPDATES = 0;
SET DEFAULT_STORAGE_ENGINE = InnoDB;

-- =============================================================================
-- 関数定義
-- =============================================================================


-- =============================================================================
-- ユーザー定義
-- =============================================================================

-- -----------------------------------------------------
-- アプリケーションユーザー
-- -----------------------------------------------------
GRANT SELECT
    , INSERT
    , UPDATE
    , DELETE
ON    `studyup_sys`.*
TO    `studyup_user`
IDENTIFIED BY 'p';

FLUSH PRIVILEGES;

-- -----------------------------------------------------
-- 管理者ユーザー
-- -----------------------------------------------------
GRANT SELECT
    , INSERT
    , UPDATE
    , DELETE
    , EXECUTE
    , SHOW VIEW
    , CREATE
    , ALTER
    , REFERENCES
    , INDEX
    , CREATE VIEW
    , CREATE ROUTINE
    , ALTER ROUTINE
    , EVENT
    , DROP
    , TRIGGER
    , CREATE TEMPORARY TABLES
    , LOCK TABLES
ON    `studyup_sys`.*
TO    `studyup_admin`
IDENTIFIED BY 'padmin';

FLUSH PRIVILEGES;

-- -----------------------------------------------------
-- 閲覧ユーザー
-- -----------------------------------------------------
GRANT SELECT
ON    `studyup_sys`.*
TO    `studyup_browse`
IDENTIFIED BY 'b';

FLUSH PRIVILEGES;

-- =============================================================================
-- テーブル定義
-- =============================================================================
-- source studyup_sys_create_table.sql

-- =============================================================================
-- 初期値投入
-- =============================================================================
-- source studyup_sys_initial_data.sql




-- -----------------------------------------------------
-- Schema studyup_test
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `studyup_test` ;

CREATE SCHEMA IF NOT EXISTS `studyup_test`
DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `studyup_test` ;

SET SQL_SAFE_UPDATES = 0;
SET DEFAULT_STORAGE_ENGINE = InnoDB;

-- =============================================================================
-- 関数定義
-- =============================================================================


-- =============================================================================
-- ユーザー定義
-- =============================================================================

-- -----------------------------------------------------
-- アプリケーションユーザー
-- -----------------------------------------------------
GRANT SELECT
    , INSERT
    , UPDATE
    , DELETE
ON    `studyup_test`.*
TO    `studyup_user`
IDENTIFIED BY 'p';

FLUSH PRIVILEGES;

-- -----------------------------------------------------
-- 管理者ユーザー
-- -----------------------------------------------------
GRANT SELECT
    , INSERT
    , UPDATE
    , DELETE
    , EXECUTE
    , SHOW VIEW
    , CREATE
    , ALTER
    , REFERENCES
    , INDEX
    , CREATE VIEW
    , CREATE ROUTINE
    , ALTER ROUTINE
    , EVENT
    , DROP
    , TRIGGER
    , CREATE TEMPORARY TABLES
    , LOCK TABLES
ON    `studyup_test`.*
TO    `studyup_admin`
IDENTIFIED BY 'padmin';

FLUSH PRIVILEGES;

-- -----------------------------------------------------
-- 閲覧ユーザー
-- -----------------------------------------------------
GRANT SELECT
ON    `studyup_test`.*
TO    `studyup_browse`
IDENTIFIED BY 'b';

FLUSH PRIVILEGES;

-- =============================================================================
-- テーブル定義
-- =============================================================================
-- source studyup_sys_create_table.sql

-- =============================================================================
-- 初期値投入
-- =============================================================================
-- source studyup_sys_initial_data.sql
