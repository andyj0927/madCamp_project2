const { User } = require('../../../lib/models')

// Post Register../..
const register = (req, res) => {
	const {userName, displayName, password} = req.body;

	console.log(userName, " ", password)

	User.create({
		name: userName,
		displayName: displayName,
		password: password
	})
}
