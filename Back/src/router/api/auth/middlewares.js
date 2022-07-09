const jwt = require('../../../lib/jwt')
const TOKEN_EXPIRED = -3
const TOKEN_INVALID = -2

const authUtil = {
	checkToken: async (req, res, next) => {
		var token = req.headers.token

		if(!token || token === "") return res.status(403).send()

		const user = await jwt.verify(token)

		if(user === TOKEN_EXPIRED)
			res.status(401).send()

		if(user === TOKEN_INVALID)
			res.status(401).send()

		if(user.id === undefined)
			return res.status(401).send()

		req.id = user.id
		next()
	}
}

module.exports = authUtil
