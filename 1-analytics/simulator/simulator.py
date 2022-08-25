import random, time, json, uuid, os
from kafka import KafkaProducer

#Iterations for each event type
newUserCount        = 500
gameStartCount      = 350
errorCount          = 50
transactionCount    = 100
gameCompletionCount = 100
delay               = 1000

versions            = [ "v1", "v2", "v3"]
levels              = [1,2,3,4,5]

redPandaHostPort      = os.getenv('REDPANDA_ADDR', 'localhost:9092')
rpTopic               = 'gaming.events'

def generateNewRegistration():
    return {
        "id": str(uuid.uuid4()),
        "player_id": random.randint(1,10000),
        "type": "NEW_USER",
        "app_version": random.choice(versions),
        "ts": int(time.time()),
        "payload": {}
    }

def generateGameStart():
    return {
        "id": str(uuid.uuid4()),
        "player_id": random.randint(1,10000),
        "type": "GAME_STARTED",
        "ts": int(time.time()),
        "payload": {}
    }

def generateError():
    return {
        "id": str(uuid.uuid4()),
        "player_id": random.randint(1,10000),
        "type": "APPLICATION_ERROR",
        "app_version": random.choice(versions),
        "ts": int(time.time()),
        "payload": {}
    }

def generateTransaction():
    tx_amount = random.randint(100,1000)/100
    payload = { "amount" : tx_amount}
    event = {
        "id": str(uuid.uuid4()),
        "player_id": random.randint(1,10000),
        "type": "TRANSACTION",
        "ts": int(time.time())
    }
    event['payload'] = payload
    return event

def generateGameCompletion():
    return {
        "id": str(uuid.uuid4()),
        "player_id": random.randint(1,10000),
        "type": "GAME_COMPLETED",
        "ts": int(time.time()),
        "payload": {"level_completed": random.choice(levels)}
    }

#Initialize Redpanda
producer = KafkaProducer(bootstrap_servers=[redPandaHostPort],
                        value_serializer=lambda x:
                        json.dumps(x).encode('utf-8'))


print("Established connection to Redpanda broker at %s", redPandaHostPort)

#First, generate new user registration events
for i in range(newUserCount):
    producer.send(
        rpTopic, 
        key=str(i).encode('ascii'), 
        value=generateNewRegistration())
    
    print("[NEW_USER] - Produced %s records.",i)

    #Pause
    time.sleep(delay/1000)

#Generate game start events
for i in range(gameStartCount):
    producer.send(
        rpTopic, 
        key=str(i).encode('ascii'), 
        value=generateGameStart())
    
    print("[GAME_START] - Produced %s records.",i)

    #Pause
    time.sleep(delay/1000)

#Generate random application error events
for i in range(errorCount):
    producer.send(
        rpTopic, 
        key=str(i).encode('ascii'), 
        value=generateError())
    
    print("[APPLICATION_ERROR] - Produced %s records.",i)

    #Pause
    time.sleep(delay/1000)

#Generate random transaction events
for i in range(transactionCount):
    producer.send(
        rpTopic, 
        key=str(i).encode('ascii'), 
        value=generateTransaction())
    
    print("[TRANSACTION] - Produced %s records.",i)

    #Pause
    time.sleep(delay/1000)

#Generate game completion events
for i in range(gameCompletionCount):
    producer.send(
        rpTopic, 
        key=str(i).encode('ascii'), 
        value=generateGameCompletion())
    
    print("[GAME_COMPLETION] - Produced %s records.",i)

    #Pause
    time.sleep(delay/1000)