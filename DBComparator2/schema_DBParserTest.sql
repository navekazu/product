-- シンプルな主キー１つ、値１つ
create table TABLE01 (
    COL01 number,
    COL02 varchar(100),
    primary key(COL01)
);

-- 主キー１つ
create table TABLE02 (
    COL01 number,
    COL02 varchar(100),
    COL03 char(100),
    COL04 date,
    primary key(COL01)
);

-- 主キー２つ
create table TABLE03 (
    COL01 number,
    COL02 varchar(100),
    COL03 char(100),
    COL04 date,
    primary key(COL01, COL02)
);
