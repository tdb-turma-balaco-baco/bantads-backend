create table if not exists users
(
    id         serial primary key,
    first_name varchar(255) not null,
    last_name  varchar(255) not null,
    email      varchar(255) not null unique,
    cpf        char(11)     not null unique,
    phone      char(11)     not null
);