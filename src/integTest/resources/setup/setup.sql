--SQL*PLUS Script to setup all the schemas required for the integTests

--Run as system

create user utp identified by utp default tablespace
  users temporary tablespace temp;

grant create session, create table, create procedure,
create sequence, create view, create public synonym,
drop public synonym to utp;

alter user utp quota unlimited on users;

create user testing identified by testing default tablespace
  users temporary tablespace temp;

grant create session, create table, create procedure,
create sequence, create view, create public synonym,
drop public synonym to testing;

alter user testing quota unlimited on users;

create user schemaOne identified by schemaOne default tablespace
  users temporary tablespace temp;

grant create session, create table, create procedure,
create sequence, create view, create public synonym,
drop public synonym to schemaOne;

alter user schemaOne quota unlimited on users;

create user schemaTwo identified by schemaTwo default tablespace
  users temporary tablespace temp;

grant create session, create table, create procedure,
create sequence, create view, create public synonym,
drop public synonym to schemaTwo;

alter user schemaTwo quota unlimited on users;

conn testing/testing@test

@../src/main/plsql/betwnstr.sf
@../src/main/plsql/simple_example.pks
@../src/main/plsql/simple_example.pkb


conn schemaOne/schemaOne@test

@../src/main/plsql/betwnstr.sf


conn schemaTwo/schemaTwo@test

@../src/main/plsql/simple_example.pks
@../src/main/plsql/simple_example.pkb