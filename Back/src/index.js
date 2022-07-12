const httpServer = require('./server')

const port = 80

httpServer.listen(port, () => {
	console.log(`Listening on 192.249.18.176:${port}`)
})

