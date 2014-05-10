# --- !Ups

CREATE TABLE movie (
  id int not null auto_increment primary key,
  title varchar(255) not null,
  year int not null
) ENGINE = InnoDB;

# --- !Downs

drop table movie;