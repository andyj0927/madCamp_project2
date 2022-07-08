require('./env')
const app = require('./app');

const { PORT } = process.env;
const port = PORT || 4000;

app.listen(PORT, () => {
	console.log(`Listening on PORT: ${PORT}.`)
})
