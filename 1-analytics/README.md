
## Event formats
GAME_COMPLETED events are used for score calculation.

Event structure:

{
    "event_id": "4234afaf23r23424",
    "player_id": 42348234,
    "score": 23,
    "ts": "2022-08-17 05:23:231"
}

## Pinot schema and tables

docker exec -it pinot-controller /opt/pinot/bin/pinot-admin.sh AddTable \
-tableConfigFile /config/events_table.json \
-schemaFile /config/events_schema.json -exec

## Pinot queries

### Total events
SELECT count(*) FROM events

### Unique users
select count(distinct player_id)
from events3

### Total revenue
TODO

### New users over time
select count(*)
from events3
where type='NEW_USER'

### Active sessions
select count(*)
from events3
where type='GAME_STARTED'

### Errors by version
select app_version, count(*)as errors
from events3
where type='APPLICATION_ERROR'
group by app_version