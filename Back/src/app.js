const express = require('express')
const path = require('path')

const router = require('./router')
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

app.use('/', router);


module.exports = app
