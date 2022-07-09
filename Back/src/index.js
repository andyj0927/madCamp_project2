require('./env')
const app = require('./app')

const { PORT } = process.env;
const port = PORT || 80;

const HTTPServer = app.listen(port, () => {
	console.log(`Listening on PORT: ${port}.`)
})

const wsModule = require('ws');

const webSocketServer = new wsModule.Server( 
    {
        server: HTTPServer,
    }
);

webSocketServer.on('connection', (ws, request)=>{

    const ip = request.headers['x-forwarded-for'] || request.socket.remoteAddress;
    
    console.log(`새로운 클라이언트[${ip}] 접속`);
    
    if(ws.readyState === ws.OPEN){
        ws.send(`클라이언트[${ip}] 접속을 환영합니다 from 서버`);
    }
    
    ws.on('message', (msg)=>{
        console.log(`클라이언트[${ip}]에게 수신한 메시지 : ${msg}`);
        ws.send('메시지 잘 받았습니다! from 서버')
    })
    
    ws.on('error', (error)=>{
        console.log(`클라이언트[${ip}] 연결 에러발생 : ${error}`);
    })
    
    ws.on('close', ()=>{
        console.log(`클라이언트[${ip}] 웹소켓 연결 종료`);
    })
});
