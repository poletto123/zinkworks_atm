DROP TABLE IF EXISTS ACCOUNT;
DROP TABLE IF EXISTS BILL;

CREATE TABLE ACCOUNT (ACCOUNT_NUMBER varchar(255), PIN varchar(255), BALANCE decimal(15,2), OVERDRAFT decimal(15,2), CONSTRAINT acct_pk PRIMARY KEY (ACCOUNT_NUMBER));
CREATE TABLE BILL (BILL_TYPE number(2), QUANTITY number(2), CONSTRAINT dispenser_pk PRIMARY KEY (BILL_TYPE));