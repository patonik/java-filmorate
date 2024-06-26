DROP TABLE IF EXISTS USER_FRIEND;
DROP TABLE IF EXISTS FILM_LIKED_BY_USER;
DROP TABLE IF EXISTS GENRE_FILM;
DROP TABLE IF EXISTS "USER";
DROP TABLE IF EXISTS FILM;
DROP TABLE IF EXISTS GENRE;
DROP TABLE IF EXISTS MPA_CODE;

CREATE TABLE IF NOT EXISTS PUBLIC.GENRE (
	ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	NAME VARCHAR_IGNORECASE(100) UNIQUE
);


CREATE TABLE IF NOT EXISTS PUBLIC.MPA_CODE (
	ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	NAME VARCHAR_IGNORECASE(25) UNIQUE
);

CREATE TABLE IF NOT EXISTS PUBLIC.FILM (
	ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	NAME VARCHAR_IGNORECASE(100),
	DESCRIPTION VARCHAR_IGNORECASE(200),
	RELEASE_DATE DATE,
	DURATION INTEGER,
	MPA_CODE INTEGER,
	CONSTRAINT FILM_MPA_CODE_FK FOREIGN KEY (MPA_CODE) REFERENCES PUBLIC.MPA_CODE(ID) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE PUBLIC.GENRE_FILM (
	ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	GENRE_ID INTEGER NOT NULL,
	FILM_ID INTEGER NOT NULL,
	CONSTRAINT GENRE_FILM_ROW_UNIQ UNIQUE(GENRE_ID, FILM_ID),
	CONSTRAINT GENRE_FILM_GENRE_FK FOREIGN KEY (GENRE_ID) REFERENCES PUBLIC.GENRE(ID) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT GENRE_FILM_FILM_FK FOREIGN KEY (FILM_ID) REFERENCES PUBLIC.FILM(ID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS PUBLIC."user" (
	ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	EMAIL VARCHAR_IGNORECASE(100) UNIQUE,
	LOGIN VARCHAR_IGNORECASE(100) UNIQUE,
	NAME VARCHAR_IGNORECASE(150),
	BIRTHDATE DATE
);

CREATE TABLE IF NOT EXISTS PUBLIC.FILM_LIKED_BY_USER (
	ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	FILM INTEGER NOT NULL,
	"user" INTEGER NOT NULL,
	CONSTRAINT FILM_LIKED_BY_USER_FILM_FK FOREIGN KEY (FILM) REFERENCES PUBLIC.FILM(ID) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT FILM_LIKED_BY_USER_USER_FK FOREIGN KEY ("user") REFERENCES PUBLIC."user"(ID) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT USER_FAVE_ROW_UNIQ UNIQUE(FILM, "user")
);


CREATE TABLE IF NOT EXISTS PUBLIC.USER_FRIEND (
	ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	"user" INTEGER NOT NULL,
	FRIEND INTEGER NOT NULL,
	CONFIRMED BOOLEAN NOT NULL,
	CONSTRAINT USER_FRIEND_USER_FK FOREIGN KEY ("user") REFERENCES PUBLIC."user"(ID) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT USER_FRIEND_USER_FK_1 FOREIGN KEY (FRIEND) REFERENCES PUBLIC."user"(ID) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT USER_FRIEND_ROW_UNIQ UNIQUE("user", FRIEND)
);
