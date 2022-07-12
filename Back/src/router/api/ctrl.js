const { Users } = require('../../lib/database/models')

const getUserList = async (req, res) => {
	try {
		const userList = await Users.findAll()
		
		res.status(200).send({
			success: true,
			message: "STATUS OK",
			data: userList
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
	getUserList,
}
