
# Project setup

create a database name "bank_management" by

```mysql
create database bank_management;
```

create a table name "users"

```mysql

create table users(
id int auto_increment,
first_name varchar(50),
last_name varchar(100),
age int,
nationality varchar(100),
id_card varchar(50),
phone varchar(50),
email varchar(100),
password varchar(255),
is_active boolean default 1,
is_admin boolean default 0,
password_salt varchar(255),
primary key (id)
);

```

dump in some data

```mysql

INSERT INTO `bank_management`.`users` (`first_name`, `last_name`, `age`, `nationality`, `id_card`, `phone`, `email`, `password`) VALUES ('Tykea', 'Ly', 19, 'Cambodian', '1244234', '4534525', 'Tykeaboyloy@gmail.com', '123456789');

```

create a table name "account"

```mysql
create table account(
account_number int auto_increment,
user_id int,
account_name varchar(50),
account_type varchar(50),
balance double,
is_active boolean default true,
foreign key (user_id) references users(id),
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
account_no int,
amount double not null default 0,
date date,
time time,
foreign key (user_id) references users(id),
foreign key (account_no) references account(account_number),
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
account_no int,
amount double not null default 0,
date date,
time time,
foreign key (user_id) references users(id),
foreign key (account_no) references account(account_number),
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
account_no int,
amount double not null default 0,
message text,
receiver_id int,
date date,
time time,
foreign key (user_id) references users(id),
foreign key (account_no) references account(account_number),
primary key(transfer_id)
);
```

dump in some data

```mysql
INSERT INTO `bank_management`.`transfer` (`transfer_id`,`user_id`,`message`,`amount`,`receiver_id`,`date`,`time`) VALUES ('1','1','','100','2','2023-03-10','15:52:44');
INSERT INTO `bank_management`.`transfer` (`transfer_id`,`user_id`,`message`,`amount`,`receiver_id`,`date`,`time`) VALUES ('2','2','Good morning Yay!','225','3','2023-03-10','15:56:19');
```

run the project to see if the input data appear in the terminal;
