ImageServer/ImageClient : client에서 이미지를 server로 전송 서버는 이미지를 저장을 함.      
ImageServer2/ImageClient2 : tcp window 이슈로 큰 용량의 이미지는 성공적으로 보낼 수 없어서, 바이트배열을 나눠서 보내는 것으로 구현 client에서 이미지를 전송 server는 이미지를 저장 후 다시 client에게 전송 client도 이미지를 받고 저장한다.