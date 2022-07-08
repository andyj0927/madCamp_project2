const passport = require('passport')
const LocalStrategy = require('passport-local').Strategy

const { Users } = require('../database/models')


module.exports = () => {
	passport.use('local', new LocalStrategy({
			usernameField: 'userName',
			passwordField: 'password',
			session: true
		}, async (userName, password, done) => {
			try {
				const exUser = await Users.findOne({ where: { userName } })
				if (exUser) {
					if(password == exUser.password) {
						done(null, exUser)
					} else {
						done(null, false, { message: 'password problem' })
					}
				}
				else {
					done(null, false, { message: "not registered" })
				}
			} catch(err) {
				console.log(err)
				done(err)
			}
		})
	)
}
