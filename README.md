# snake a netty nio game server framework.

1. MessageCreator a java object pool based on Netty

optimized for Netty, serverside ZeroCopy, memory used by message pool and Netty less than 2G,.

Performance improvement at least doubled than ProBuffer on server side.

2. Connector transfer's client message to server

how to test: 

1. open AppTestLoginCenter
2. open Server
3. open AppConnector

4. invoke 

http://localhost:8080/userloaded/ipPort=127.0.0.1:6666/token=dsfklsdfjkl390890238/uuid=16545682/transferIp=127.0.0.1/transferPort=7777

http://localhost:8080/userloaded/ipPort=127.0.0.1:6666/token=dsfklsdfjkl390890238d3ddd/uuid=165433335682/transferIp=127.0.0.1/transferPort=7777

5. open Client and Client2