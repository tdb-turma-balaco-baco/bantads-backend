create schema if not exists auth;

create table if not exists auth.users (
    id serial primary key,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    email varchar(255) not null unique,
    password text not null
);

grant select, insert, update, delete
    on all tables
    in schema auth
    to bantads;