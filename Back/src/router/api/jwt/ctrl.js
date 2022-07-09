const jwt = require('../../../lib/jwt')
const { Users } = require('../../../lib/database/models')

const getJwt = (req, res) => {
	try {
		const user = await Users.findOne({
			where: {
				id: req.id
			}
		})

		const token = await jwt.sign(user).token

		res.status(200).send({
			success: true,
			message: "Json Web Token",
			data: token
		})
	} catch(e) {
		console.error(e)
		res.status(500).send({
			success: false,
			message: "INTERNAL SERVER ERROR",
			data: null
		})
	}
}

module.exports = {
	getJwt
}
