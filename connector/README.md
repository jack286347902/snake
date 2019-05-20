# Connector transfer's client message to server

1. message format:

client message: 

size 			short
cmd				short
data			byte[]
validate		byte

server message: 

size 			int
cmd				short
data			byte[]
validate		byte

2. Encrypt and Decrypt message in connector
	use user token as crypt key


3. how to test: 

1. open AppTestLoginCenter
2. open Server
3. open AppConnector

4. invoke 

http://localhost:8080/userloaded/ipPort=127.0.0.1:6666/token=dsfklsdfjkl390890238/uuid=16545682/transferIp=127.0.0.1/transferPort=7777

http://localhost:8080/userloaded/ipPort=127.0.0.1:6666/token=dsfklsdfjkl390890238d3ddd/uuid=165433335682/transferIp=127.0.0.1/transferPort=7777

5. open Client and Client2