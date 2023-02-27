CREATE TABLE IF NOT EXISTS STATS
(
    ID   int4 generated always as identity primary key,
    APP  varchar(256) NOT NULL,
    URI  varchar,
    IP   varchar,
    TIME timestamp
);