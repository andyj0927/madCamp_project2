require('./env')
const app = require('./app')

const { PORT } = process.env;
const port = PORT || 80;

app.listen(port, () => {
	console.log(`Listening on PORT: ${port}.`)
})
