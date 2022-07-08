const { Users } = require('../../../lib/database/models')
const passport = require('passport')
const sequelize = require('sequelize')
const Op = sequelize.Op

// POST Register
const register = async (req, res) => {
	console.log(req.body)
	const {userName, displayName, password} = req.body

	try {
		console.log("before findAll")
		const user = await Users.findAll({
			where: {
				[Op.or]:[ { userName: userName }, { displayName: displayName }]
			}
		})
		console.log("after findAll: " + user)
		if(user === "" || user === undefined || user === null) {
			const data = {
				userName: userName,
				displayName: displayName,
				password: password,
				friends: null,
			}
			console.log("before insert data")
			await Users.create(data)
			console.log("after insert data")
			res.status(200).send({ data })
		}
		else {
			res.status(400).send("Exists id or displayName")
		}
		
	} catch(e){
		res.status(500).send("Internal Server error")
	}
}

// POST Login
const login = async (req, res, next) => {
	passport.authenticate('local', (authError, user, info) => {
		if(authError) {
			console.error(authError)
			return next(authError)
		}

		if(!user) {
			return res.status(400).send("BAD REQUEST")
		}

		return req.login(user, loginError => {
			if(loginError) {
				console.error(loginError)
				return next(loginError)
			}

			return res.status(200).send({data: user})
		})
	})(req, res, next)
}

// GET Logout
const logout = (req, res, next) => {
	try {
		console.log("Debug 1")
		req.session.destroy()
		console.log("Debug 2")
		res.status(200).send("Logout success")
	} catch(e) {
		console.error(e)
		res.status(500).send("internal server error")
	}
}

const getUserList = async (req, res) => {
	try {
		const allUsers = Users.findAll()

		res.status(200).send({ data: allUsers })
	} catch(e) {
		console.error(e)
		res.status(500).send("Internal Server Error")
	}
}

module.exports = {
	getUserList,
	register,
	login,
	logout
}
