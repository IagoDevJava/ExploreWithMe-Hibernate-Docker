CREATE TABLE IF NOT EXISTS STATS
(
    ID   BIGINT generated always as identity primary key,
    APP  varchar(256) NOT NULL,
    URI  varchar(256),
    IP   varchar(256),
    TIME timestamp    NOT NULL
);