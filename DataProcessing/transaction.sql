DELIMITER //
drop table IF EXISTS bank_tran//
drop table IF EXISTS atm_log//
drop procedure IF EXISTS dispenseCash//
drop procedure IF EXISTS saveTran//

create table bank_tran(
reference_id bigint primary key auto_increment,
account_number char(16) not null,
tran_amount bigint not null,
tran_type char(1) not null
)//

create table atm_log(
reference_id bigint primary key auto_increment,
card_number char(16) not null,
tran_amount bigint not null
)//

create procedure dispenseCash(tran_amount bigint,out dispensed int)
Begin
IF tran_amount<=10000 then
set dispensed=1;
ELSE
set dispensed=0;
END IF;
END; //

create procedure saveTran(card_number char(16),account_number char(16),tran_amount bigint)
Begin
DECLARE dispensed int;
SET AUTOCOMMIT=0;
START TRANSACTION;
insert into atm_log(card_number,tran_amount) values(card_number,tran_amount);
SAVEPOINT logged;
insert into bank_tran(account_number,tran_amount,tran_type) values (account_number,tran_amount,'W');
call dispenseCash(tran_amount,dispensed);
IF dispensed=0 THEN
ROLLBACK to SAVEPOINT logged;
END IF;
COMMIT;
END; //

DELIMITER ;