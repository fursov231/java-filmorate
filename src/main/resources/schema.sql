
create table IF NOT EXISTS USERS
(
    USER_ID  INTEGER auto_increment,
    EMAIL    VARCHAR(64) not null unique,
    LOGIN    VARCHAR(64) not null,
    NAME     VARCHAR(64) not null,
    BIRTHDAY TIMESTAMP            not null,
    constraint USER_TABLE_PK
        primary key (USER_ID)
);

create table IF NOT EXISTS FRIENDS
(
    OUTGOING_USER_ID INTEGER not null,
    INCOMING_USER_ID INTEGER not null,
    STATUS           VARCHAR(64) not null,
    constraint FRIENDS_PK
        primary key (OUTGOING_USER_ID, INCOMING_USER_ID),
    constraint FRIENDS_USERS_USER_ID_FK
        foreign key (OUTGOING_USER_ID) references USERS,
    constraint FRIENDS_USERS_USER_ID_FK_2
        foreign key (INCOMING_USER_ID) references USERS
);

create table IF NOT EXISTS GENRE
(
    GENRE_ID INTEGER              not null,
    NAME     VARCHAR(64) not null,
    constraint GENRE_PK
        primary key (GENRE_ID)
);

create table IF NOT EXISTS MPA
(
    MPA_ID INTEGER not null,
    NAME   VARCHAR(64),
    constraint MPA_PK
        primary key (MPA_ID)
);

create table IF NOT EXISTS FILMS
(
    FILM_ID      INTEGER auto_increment,
    NAME         VARCHAR(64) not null unique,
    DESCRIPTION  VARCHAR(64) not null,
    RELEASE_DATE TIMESTAMP            not null,
    DURATION     VARCHAR(64) not null,
    RATE         INTEGER,
    MPA_ID       INTEGER              not null,
    constraint FILM_PK
        primary key (FILM_ID),
    constraint FILMS_MPA_MPA_ID_FK
        foreign key (MPA_ID) references MPA
);

create table IF NOT EXISTS LIKES
(
    FILM_ID INTEGER not null,
    USER_ID INTEGER not null,
    constraint LIKES_PK
        primary key (USER_ID, FILM_ID),
    constraint LIKES_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS,
    constraint LIKES_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS
);

create table IF NOT EXISTS FILM_GENRES
(
    FILM_ID  INTEGER not null,
    GENRE_ID INTEGER not null,
    constraint FILM_GENRES_PK
        primary key (FILM_ID, GENRE_ID),
    constraint FILM_GENRES_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS,
    constraint FILM_GENRES_GENRE_GENRE_ID_FK
        foreign key (GENRE_ID) references GENRE
);

