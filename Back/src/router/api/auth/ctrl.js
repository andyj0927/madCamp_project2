const { Users } = require('../../../lib/database/models')

// Post Register../..
const register = (req, res) => {
	console.log(req.body)
	const {userName, displayName, password} = req.body;

	const newUser = {
		userName: userName,
		displayName: displayName,
		password: password
	}

	try {
		Users.create(newUser)
		console.log(newUser)
		res.status(200).send({data: newUser})
	} catch(e){
		console.log(e)
		res.status(500).send("internalServerError")
	}
}

module.exports = {register}
