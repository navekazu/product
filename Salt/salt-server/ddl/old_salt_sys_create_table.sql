-- -----------------------------------------------------
drop table if exists `member`;
create table `member` (
    `member_id`             INT             NOT NULL AUTO_INCREMENT                 COMMENT 'ID',
    `name`                  VARCHAR(100)    NOT NULL                                COMMENT '名称',
    `login_id`              VARCHAR(100)    NOT NULL                                COMMENT 'ログインID',
    `password`              VARCHAR(100)    NOT NULL                                COMMENT 'パスワード (BCrypt)',
    `mail_address`          VARCHAR(100)    NOT NULL                                COMMENT 'メールアドレス',
    `kind`                  INT             NOT NULL                                COMMENT '種別 (0:管理者, 1:一般)',
    `status`                INT             NOT NULL                                COMMENT 'ステータス (0:登録, 1:有効, 2:無効, 3:削除)',
    `created_at`            DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP()    COMMENT '登録日時',
    `updated_at`            DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP()
                                                   ON UPDATE CURRENT_TIMESTAMP()    COMMENT '更新日時',
    primary key(`member_id`)
)
COMMENT = 'メンバー';

-- -----------------------------------------------------
drop table if exists `question_master`;
create table `question_master` (
    `question_master_id`    INT             NOT NULL AUTO_INCREMENT                 COMMENT 'ID',
    `name`                  VARCHAR(100)    NOT NULL                                COMMENT '名前',
    `choice_count`          INT             NOT NULL                                COMMENT '選択肢数',
    `created_at`            DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP()    COMMENT '登録日時',
    `updated_at`            DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP()
                                                   ON UPDATE CURRENT_TIMESTAMP()    COMMENT '更新日時',
    primary key(`question_master_id`)
)
COMMENT = '質問マスター';

-- -----------------------------------------------------
drop table if exists `question`;
create table `question` (
    `question_id`           INT             NOT NULL AUTO_INCREMENT                 COMMENT 'ID',
    `question_master_id`    INT             NOT NULL                                COMMENT '質問マスターID',
    `contents`              VARCHAR(1000)   NOT NULL                                COMMENT '問題',
    primary key(`question_id`),
    constraint `fk_question`
        foreign key (`question_master_id`)
            references `question_master` (`question_master_id`)
                on delete cascade
                on update cascade
)
COMMENT = '質問';
create index `id_question__question_master_id` on `question` (
    question_master_id
);

-- -----------------------------------------------------
drop table if exists `question_choice`;
create table `question_choice` (
    `question_choice_id`    INT             NOT NULL AUTO_INCREMENT                 COMMENT 'ID',
    `question_id`           INT             NOT NULL                                COMMENT '質問ID',
    `label`                 VARCHAR(100)    NOT NULL                                COMMENT '選択肢',
    `correct_flag`          BOOLEAN         NOT NULL                                COMMENT '正解フラグ',
    primary key(`question_choice_id`),
    constraint `fk_question_choice`
        foreign key (`question_id`)
            references `question` (`question_id`)
                on delete cascade
                on update cascade
)
COMMENT = '選択肢';
create index `id_question_choice__question_id` on `question_choice` (
    question_id
);

-- -----------------------------------------------------
drop table if exists `answer_master`;
create table `answer_master` (
    `answer_master_id`      INT             NOT NULL AUTO_INCREMENT                 COMMENT 'ID',
    `member_id`             INT             NOT NULL                                COMMENT 'メンバーID',
    `question_master_id`    INT             NOT NULL                                COMMENT '質問マスターID',
    `created_at`            DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP()    COMMENT '登録日時',
    primary key(`answer_master_id`),
    constraint `fk_answer_master`
        foreign key (`member_id`)
            references `member` (`member_id`)
                on delete cascade
                on update cascade
)
COMMENT = '回答マスター';
create index `id_answer_master__member_id` on `answer_master` (
    member_id
);

-- -----------------------------------------------------
drop table if exists `answer`;
create table `answer` (
    `answer_id`             INT             NOT NULL AUTO_INCREMENT                 COMMENT 'ID',
    `answer_master_id`      INT                                                     COMMENT '回答マスターID',
    `question_choice_id`    INT                                                     COMMENT '選択肢ID',
    primary key(`answer_id`),
    constraint `fk_answer`
        foreign key (`answer_master_id`)
            references `answer_master` (`answer_master_id`)
                on delete cascade
                on update cascade
)
COMMENT = '回答';
create index `id_answer__answer_master_id` on `answer` (
    answer_master_id
);
