# Project setup

create a database name "bank_management" by

```mysql
create database bank_management;
```

create a table name "users"

```mysql
create table users(
user_id int auto_increment,
name varchar(50),
email varchar(100),
primary key(user_id)
) ;
```

dump in some data

```mysql
INSERT INTO `bank_management`.`users` (`name`, `email`) VALUES ('pichey', 'tykeaboyloy@gmail.com');
```

create a table name "account"

```mysql
create table account(
account_number int auto_increment,
user_id int,
account_name varchar(50),
account_type varchar(50),
balance double,
foreign key (user_id) references users(user_id),
primary key(account_number)
);
```

dump in some data

```mysql
INSERT INTO `bank_management`.`account` (`account_number`, `user_id`, `account_name`, `account_type`, `balance`) VALUES (123456788, 1, 'pichey', 'ffdsfsdfads', 999999);
```

create a table name "deposit"

```mysql
create table deposit(
deposit_id int auto_increment,
user_id int,
amount double default 0,
date date,
time time,
foreign key (user_id) references users(user_id),
primary key(deposit_id)
);
```

dump in some data

```mysql
INSERT INTO `bank_management`.`deposit` (`deposit_id`,`user_id`,`amount`,`date`,`time`) VALUES ('1','1','100','2023-03-10','15:46:33');
```

create a table name "withdraw"

```mysql
create table withdraw(
withdrawn_id int auto_increment,
user_id int,
amount double default 0,
date date,
time time,
foreign key (user_id) references users(user_id),
primary key(withdrawn_id)
);
```

dump in some data

```mysql
INSERT INTO `bank_management`.`withdraw` (`withdrawn_id`,`user_id`,`amount`,`date`,`time`) VALUES ('1','1','400','2023-03-10','15:48:56');
```

create a table name "transfer"

```mysql
create table transfer(
transfer_id int auto_increment,
user_id int,
amount double default 0,
receiver_id int,
date date,
time time,
foreign key (user_id) references users(user_id),
primary key(transfer_id)
);
```

dump in some data

```mysql
INSERT INTO `bank_management`.`transfer` (`transfer_id`,`user_id`,`message`,`amount`,`receiver_id`,`date`,`time`) VALUES ('1','1','','100','2','2023-03-10','15:52:44');
INSERT INTO `bank_management`.`transfer` (`transfer_id`,`user_id`,`message`,`amount`,`receiver_id`,`date`,`time`) VALUES ('2','2','Good morning Yay!','225','3','2023-03-10','15:56:19');
```

run the project to see if the input data appear in the terminal;
