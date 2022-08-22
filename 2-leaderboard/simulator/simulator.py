import random, time, json, uuid, os
from kafka import KafkaProducer

iterations  = 1000
delay       = 1000

redPandaHostPort      = os.getenv('REDPANDA_ADDR', 'localhost:9092')
rpTopic               = 'gaming.scores'

def generateGameEnd():
    return {
        "id": str(uuid.uuid4()),
        "player_id": random.randint(1,10000),
        "score": random.randint(10,1000),
        "ts": int(time.time())
    }

#Initialize Redpanda
producer = KafkaProducer(bootstrap_servers=[redPandaHostPort],
                        value_serializer=lambda x:
                        json.dumps(x).encode('utf-8'))


print("Established connection to Redpanda broker at %s", redPandaHostPort)

for i in range(iterations):

    producer.send(
        rpTopic, 
        key=str(i).encode('ascii'), 
        value=generateGameEnd())
    
    print("Produced %s records.",i)

    #Pause
    time.sleep(delay/1000)


