# --- !Ups

CREATE TABLE actor (
  id int not null auto_increment primary key,
  firstName varchar(255) not null,
  lastName varchar(255) not null
) ENGINE = InnoDB;

# --- !Downs

drop table actor;