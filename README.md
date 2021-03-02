# build and run
- go to consumer_app and run ```mvn clean install```
- go to producer_app and run ```mvn clean install```

- go to root folder and run ```docker-compose up -d --build```
- consumer_app is failing because **testTopic** is not created on kafka! 
- run ```curl -i -X GET http://localhost:8081/chat```
to create a topic testTopic by calling the chat endpoint
- start consumer_app again since the topic is created: ```docker-compose start consumer_app```
- now everything should be up and running:
:)
