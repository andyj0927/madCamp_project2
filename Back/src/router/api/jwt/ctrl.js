const jwt = require('../../../lib/jwt')

// POST /api/jwt/sign
// to set and get json web token
const setJwt = async (req, res) => {
	try {
		console.log("/api/jwt/sign")
		console.log(req.body)
		const { id } = req.body
		console.log(id)
		const promiseToken = await jwt.sign(id)
		const token = promiseToken.token

		console.log(token)

		res.status(200).send({
			success: true,
			message: "STATUS OK",
			data: token
		})
	} catch(e){
		console.error(e)
		res.status(500).send({
			success: false,
			message: "INTERNAL SERVER ERROR",
			data: null
		})
	}
}

module.exports = {
	setJwt
}
