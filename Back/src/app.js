const express = require('express')
const router = require('./router')
const passport = require('passport')
const passportConfig = require('./lib/passport')
const session = require('express-session')
const { sequelize } = require('./lib/database/models')


const app = express()

sequelize.sync({force: false})
	.then(() => {
		console.log("DB Connected Success")
	}).catch(err => {
		console.error(err)
	})


app.use(express.urlencoded({extended: true}))
app.use(express.json())

app.use(session({
	secret: '!@#$%^&*()1234567890',
	resave: false,
	saveUninitialized: true,
	cookie: {
		httpOnly: true,
		secure: false,
		maxAge: 1000 * 60 * 60 * 24 * 7 
	},
}))

app.use(passport.initialize())
app.use(passport.session())

passportConfig()

app.use('/', router)

module.exports = app
