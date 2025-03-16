use smallbox;
drop table if exists reservation;
drop table if exists login;
drop table if exists loginAttempt;
drop table if exists saltInfo;
drop table if exists member;

-- 회원 정보 테이블
create table member(
userId int auto_increment primary key,
email varchar(50) not null unique,
pwd varchar(256) not null,
userName varchar(20) not null,
registDate timestamp not null default current_timestamp
);

-- 비밀번호 솔트 저장 테이블
create table saltInfo(
userId int primary key,
salt varchar(256) not null,
foreign key (userId) references member(userId) on delete cascade -- 회원 탈퇴 시 saltInfo, login 자동 해제
);

-- 로그인 정보 테이블
create table login(
userId int primary key, -- 이메일 대신 사용 + 여러 기기에서 로그인 할 수 있도록 unique 안 넣음
token varchar(512) not null,
loginTime timestamp not null default current_timestamp,
foreign key (userId) references member(userId) on delete cascade
);

-- 로그인 횟수 제한 테이블
create table loginAttempt(
email varchar(50) primary key,
attemptCount int default 0,
lastAttempt timestamp default current_timestamp,
isLocked int default 0
);

-- 예매 정보 테이블
create table reservation (
reservationId int auto_increment primary key,
userId int not null,
movieTitle varchar(100) not null,              -- 영화 제목 (API 전달)
theaterName varchar(100) not null,             -- 극장명 (API 전달)
scheduleTime varchar(20) not null,                -- 상영 시간 (API 전달)
seatNumber varchar(10) not null,               -- 좌석 번호 (API 전달)
bookingTime timestamp default current_timestamp, -- 예매 시간
foreign key (userId) references member(userId) on delete cascade
);

commit;