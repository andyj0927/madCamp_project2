const { Users } = require('../lib/database/models')

const getUserList = (req, res) => {
	try {
		const userList = Users.findAll()

		console.log(userList.size)
		res.status(200).send({
			data: userList
		})
	} catch (e) {
		res.status(500).send("Internal Server Error")
	}
}

module.exports = {
	getUserList,
}
