const jwt = require('../../../lib/jwt')
const TOKEN_EXPIRED = -3
const TOKEN_INVALID = -2

const authUtil = {
	checkToken: async (req, res, next) => {
		const token = req.headers.token

		if(!token || token === "") {
			console.log("token is null")
			return res.status(400).send({
				success: false,
				message: "BAD REQUEST",
				data: null
			})
		}

		const ret = await jwt.verify(token)
		if(ret === TOKEN_EXPIRED)
			return res.status(401).send({
				success: false,
				message: "UNAUTHORIZED",
				data: null
			})

		if(ret === TOKEN_INVALID)
			return res.status(401).send({
				success: false,
				message: "UNAUTHORIZED",
				data: null
			})

		if(ret == null || ret == undefined)
			return res.status(401).send({
				success: false,
				message: "UNAUTHORIZED",
				data: null
			})

		req.id = ret.id 
		next()
	}
}

module.exports = authUtil
