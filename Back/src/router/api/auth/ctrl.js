const { Users } = require('../../../lib/database/models')
const sequelize = require('sequelize')
const jwt = require('../../../lib/jwt')

const Op = sequelize.Op

// POST /api/auth/register
// to register user
const register = async (req, res) => {
	console.log(req.body)
	const {userName, displayName, password} = req.body

	try {
		console.log("before findAll")
		const user = await Users.findOne({
			raw: true,
			where: {
				[Op.or]:[ { userName: userName }, { displayName: displayName }]
			}
		})
		if(user == null) {
			const data = {
				userName,
				displayName,
				password,
				friends: null,
				google: null,
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

// POST /api/auth/login
// to sign in
const login = async (req, res) => {
	const { userName, password } = req.body
	console.log(userName)
	const user = await Users.findOne({
		raw: true,
		where: {
			userName: userName
		}
	})

	await Users.update({
		currentlyActive: 1,
	}, {
		where: {
			id: user.id,
		}
	})

	console.log(user)
	if(!user) {
		res.status(403).send()
		return
	}

	if(user.password !== password) {
		res.status(400).send()
		return
	}
	
	res.status(200).send({
		success: true,
		message: "OK",
		data: user,
	})
}

// GET /api/auth/logout
// to sign out
const logout = async (req, res) => {
	try {
		const userId = req.id

		await Users.update({
			currentlyActive: 0
		}, {
			where: {
				id: userId
			}
		})

		res.status(200).send({
			success: true,
			message: "logout Success"
		})
	} catch(e) {
		console.error(e)
		res.status(500).send()
	}
}

// GET /api/auth/user-list
// to get all users for main activity
const getUserList = async (req, res) => {
	try {
		const allUsers = await Users.findAll({
			raw: true
		})

		res.status(200).send({ data: allUsers })
	} catch(e) {
		console.error(e)
		res.status(500).send("Internal Server Error")
	}
}

// POST /api/auth/google/login
// to get one user by googleToken
const getUserByGoogleToken = async (req, res) => {
	try {
		console.log(req.body)
		const googleToken = req.body

		const data = await Users.findOne({
			raw: true,
			where: {
				google: googleToken
			}
		})

		const jwtToken = await jwt.sign(data)
		if(data) {
			res.status(200).send({
				data,
				token: jwtToken.token
			})
		}
	} catch(e){
		console.error(e)

	}
}

// POST /api/auth/google/interlock
// to interlock between local account and google account
const interlockBetweenLocalAndGoogle = async (req, res) => {
	try {
		console.log(req.body)
		const googleToken = req.body
		const userId = req.id

		await Users.update({
			google: googleToken,
		}, {
			where: {
				id: userId
			}
		})
		res.send(200).send()
	} catch (e) {
		console.error(e)
		res.send(500).send({
			message: "INTERNAL SERVER ERROR"
		})
	}
}

// GET /api/auth/Info/{userId}
// to get information of certail user
const getUserInfo = async (req, res) => {
	try {
		const userId = req.params.userId

		const user = await Users.findOne({
			where: {
				id: userId
			}
		})

		if(user == null) {
			res.status(404).send({
				success: false,
				message: "PAGE NOT FOUND",
				data: null
			})
		} 

		res.status(200).send({
			success: true,
			message: "STATUS OK",
			data: user
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
	getUserList,
	register,
	login,
	logout,
	getUserInfo,
	getUserByGoogleToken,
	interlockBetweenLocalAndGoogle
}
