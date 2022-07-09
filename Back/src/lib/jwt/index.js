const randToken = require('rand-token')
const jwt = require('jsonwebtoken')
const config = require('../../config/jwt')

const TOKEN_EXPIRED = -3
const TOKEN_INVALID = -2

module.exports = {
	sign: async user => {
		const payload = {
			id: user.id
		}
		const result = {
			token: jwt.sign(payload, config.secret, config.option),
			refreshToken: randToken.uid(256)
		}
		return result
	},
	verify: async (token) => {
		let decoded
		try {
			decoded = jwt.verify(token, config.secret)
		} catch(err) {
			if (err.message === 'jwt expired') {
				console.log('expired token')
				return TOKEN_EXPIRED
			} else if (err.message === 'invalid token') {
				console.log(TOKEN_INVALID)
				return TOKEN_INVALID
			}
			else {
				console.log("invalid token")
				return TOKEN_INVALID
			}
		}
		return decoded
	}
}
