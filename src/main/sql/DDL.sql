-- 创建数据库
CREATE DATABASE IF NOT EXISTS crawler;
-- 创建record表
CREATE TABLE IF NOT EXISTS record(
recordId int (5) not null auto_increment,
 URL text not null,
 crawled tinyint(1) not null,
 primary key (recordID)
 ) engine=InnoDB DEFAULT CHARSET=utf8;
--  创建tags表
CREATE TABLE IF NOT EXISTS tags(
tagnum int(4) not null auto_increment,
tagname text not null,
primary key (tagnum)
) engine=InnoDB DEFAULT CHARSET=utf8;
