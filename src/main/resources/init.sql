CREATE user C##TUNE identified by TEST DEFAULT tablespace users temporary tablespace temp account unlock;
GRANT CONNECT, RESOURCE TO C##TUNE
GRANT CREATE TABLE TO C##TUNE
GRANT unlimited TABLESPACE TO C##TUNE

CREATE TABLE "C##TUNE".artist (
    ARTIST_ID VARCHAR2(255) PRIMARY KEY,
    ARTIST_NAME VARCHAR2(255) NOT NULL
);

CREATE TABLE "C##TUNE"."SONG" (
    "ID" VARCHAR2(255) PRIMARY KEY,
    "SONG_NAME" VARCHAR2(255) NOT NULL,
    "ARTIST_ID" VARCHAR2(255) NOT NULL,
    "GENRE" VARCHAR2(255),
    FOREIGN KEY ("ARTIST_ID") REFERENCES "C##TUNE"."ARTIST" ("ARTIST_ID")
);

CREATE TABLE "C##TUNE"."USER" (
    "ID" VARCHAR2(255) PRIMARY KEY
);

CREATE TABLE "C##TUNE"."RATING" (
    "ID" VARCHAR2(255) PRIMARY KEY,
    "SONG_ID" VARCHAR2(36) NOT NULL,
    "USER_ID" VARCHAR2(36) NOT NULL,
    "RATING" NUMBER(1) CHECK ("RATING" BETWEEN 1 AND 5),
    "RATING_DATE" DATE NOT NULL,
    FOREIGN KEY ("SONG_ID") REFERENCES "C##TUNE"."SONG" ("ID"),
    FOREIGN KEY ("USER_ID") REFERENCES "C##TUNE"."USER" ("ID")
);

CREATE INDEX idx_ratings_song_id ON "C##TUNE"."RATING" ("SONG_ID");
CREATE INDEX idx_ratings_user_id ON "C##TUNE"."RATING" ("USER_ID");
CREATE INDEX idx_ratings_rating_date ON "C##TUNE"."RATING" ("RATING_DATE");