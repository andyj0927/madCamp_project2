const express = require('express')
const router = require('./router')
const session = require('express-session')
const { sequelize } = require('./lib/database/models')

sequelize.sync({force: false})
	.then(() => {
		console.log("DB Connected Success")
	}).catch(err => {
		console.error(err)
	})

const app = express()
app.use(express.urlencoded({extended: true}))
app.use(express.json())

app.use('/', router)
app.use(session({
	secret: '!@#$%^&*()1234567890',
	resave: false,
	saveUninitialized: true,
}))

module.exports = app
