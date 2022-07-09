const express = require('express')
const router = require('./router')
const { sequelize } = require('./lib/database/models')
const jwt = require('./config/jwt')

const app = express()

sequelize.sync({force: false})
	.then(() => {
		console.log("DB Connected Success")
	}).catch(err => {
		console.error(err)
	})

app.set('jwt-secret', jwt.secret)

app.use(express.urlencoded({extended: true}))
app.use(express.json())

app.use('/', router)

module.exports = app
