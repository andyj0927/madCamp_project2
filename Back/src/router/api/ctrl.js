const { Users } = require('../../lib/database/models')

const getUserList = async (req, res) => {
	try {
		userList = await Users.findAll()
		
		res.status(200).send({
			sessonId: req.session.sessionId,
			data: userList
		})
	} catch(e) {
		console.error(e)
		res.status(500).send({
			data: "internal server error"
		})
	}
}

module.exports = {
	getUserList,
}
