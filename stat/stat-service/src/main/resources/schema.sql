CREATE TABLE IF NOT EXISTS STATS
(
    ID   int4 generated always as identity primary key,
    APP  varchar,
    URI  varchar,
    IP   varchar,
    TIME timestamp
);