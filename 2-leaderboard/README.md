
## Event formats
GAME_COMPLETED events are used for score calculation.

Event structure:

{
    "event_id": "4234afaf23r23424",
    "player_id": 42348234,
    "score": 23,
    "ts": "2022-08-17 05:23:231"
}

## Materialize

psql -U materialize -h localhost -p 6875 materialize

## Kafka source definitions

CREATE SOURCE scores_source
  FROM KAFKA BROKER 'redpanda:29092' TOPIC 'gaming.scores'
  FORMAT BYTES;

CREATE MATERIALIZED VIEW scores AS
  SELECT
    (data->>'id') AS id,
    (data->>'player_id')::int AS player_id,
    (data->>'score')::int AS score,
    data->>'ts' AS ts
  FROM (SELECT CONVERT_FROM(data, 'utf8')::jsonb AS data FROM scores_source);

CREATE MATERIALIZED VIEW leaderboard AS
    SELECT
        player_id,
        sum(score) as total
    FROM scores
    GROUP BY player_id
    ORDER BY sum(score) DESC;
