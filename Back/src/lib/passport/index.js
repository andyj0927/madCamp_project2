const passport = require('passport')
const local = require('./localStrategy')

const { Users } = require('../database/models')

module.exports = () => {
	passport.serializeUser((user, done) => {
		done(null, user.id)
	})

	passport.deserializeUser((id, done) => {
		Users.findOne({ where: { id } })
			.then(user => done(null, user))
			.catch(err => done(err))
	})

	local()
}
