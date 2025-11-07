drop table IF EXISTS student;
drop table IF EXISTS course;
create table course
(
id int primary key auto_increment,
title char(25) not null
);
create table student
(
roll_number int primary key,
first_name char(20) not null,
last_name char(20) not null,
aadhar_card_number char(20) not null unique,
course_code int not null,
gender char(1) not null,
date_of_birth date not null,
FOREIGN KEY (course_code) references course(id)
);