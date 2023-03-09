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
foreign key (user_id) references Users(user_id),
primary key(account_number)
);
```

dump in some data

```mysql
INSERT INTO `bank_management`.`account` (`account_number`, `user_id`, `account_name`, `account_type`, `balance`) VALUES (123456788, 1, 'pichey', 'ffdsfsdfads', 999999);
```

run the project to see if the input data appear in the terminal;
