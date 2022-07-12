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
		console.log("before findOne")
		const user = await Users.findOne({
			raw: true,
			where: {
				[Op.or]:[ { userName: userName }, { displayName: displayName }]
			}
		})
		console.log("after findOne")
		if(user == null) {
			const newUser = {
				userName,
				displayName,
				password,
				friends: "{}",
				google: null,
				currentlyActive: 0
			}
			console.log("before insert data")
			await Users.create(newUser)
			const data = await Users.findOne({
				raw: true,
				attributes: ['id', 'userName', 'displayName', 'win', 'lose', 'draw', 'total', 'friends', 'currentlyActive', 'createdAt', 'updatedAt'],
				where: {
					userName: userName
				}
			})
			console.log("after insert data")
			return res.status(200).send({ data })
		}
		else {
			return res.status(400).send("Exists id or displayName")
		}
		
	} catch(e){
		console.error(e)
		return res.status(500).send("Internal Server error")
	}
}

// POST /api/auth/login
// to sign in
const login = async (req, res) => {
	try {
		console.log("POST /api/auth/login")
		const { userName, password } = req.body
		console.log(userName)
		console.log("before find User")
		const user = await Users.findOne({
			raw: true,
			attributes: ['id', 'password'],
			where: {
				userName: userName
			}
		})
		if(!user) {
			return res.status(400).send({
				success: false,
				message: "BAD REQUEST",
				data: null
			})
		}
		console.log("after findUser and before update")

		if(user.password !== password) {
			return res.status(400).send({
				success: false,
				message: "BAD REQUEST",
				data: null
			})
		}
		
		return res.status(200).send({
			success: true,
			message: "STATUS OK",
			data: user.id,
		})
		
	} catch(e){
		console.error(e)
		return res.status(500).send({
			success: false,
			message: "INTERNAL SERVER ERROR",
			data: null
		})
	}
}

// GET /api/auth/logout
// to sign out
const logout = async (req, res) => {
	try {
		return res.status(200).send({
			success: true,
			message: "logout Success"
		})
	} catch(e) {
		console.error(e)
		return res.status(500).send({
			success: false,
			message: "INTERNAL SERVER ERROR"
		})
	}
}

// GET /api/auth/user-list
// to get all users for main activity
const getUserList = async (req, res) => {
	try {
		const allUsers = await Users.findAll({
			raw: true,
			attributes: ['id', 'userName', 'displayName', 'win', 'lose', 'draw', 'total', 'friends', 'currentlyActive', 'createdAt', 'updatedAt']
		})

		return res.status(200).send({ data: allUsers })
	} catch(e) {
		console.error(e)
		return res.status(500).send({
			success: false,
			message: "INTERNAL SERVER ERROR"
		})
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
			attributes: ['id'],
			where: {
				google: googleToken
			}
		})

		if(data) {
			return res.status(200).send({
				success: true,
				message: "STATUS OK",
				data: data.id,
			})
		}
		else {
			return res.status(400).send({
				success: false,
				message: "BAD REQUEST",
				data: null
			})
		}
	} catch(e){
		console.error(e)
		return res.status(500).send({
			success: false,
			message: "INTERNAL SERVER ERROR",
			data: null
		})
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

		return res.send(200).send({
			success: true,
			message: "STATUS OK",
			data: null
		})
	} catch (e) {
		console.error(e)
		return res.send(500).send({
			success: false,
			message: "INTERNAL SERVER ERROR",
			data: null
		})
	}
}

// GET /api/auth/Info/{userId}
// to get information of certail user
const getUserInfo = async (req, res) => {
	try {
		const id = req.params.id
		const user = await Users.findOne({
			raw: true,
			attributes: ['id', 'userName', 'displayName', 'win', 'lose', 'draw', 'total', 'friends', 'currentlyActive', 'google', 'createdAt', 'updatedAt'],
			where: {
				id
			}
		})

		if(user == null) {
			res.status(404).send({
				success: false,
				message: "PAGE NOT FOUND",
				data: null
			})
		} 

		return res.status(200).send({
			success: true,
			message: "STATUS OK",
			data: user
		})

	} catch(e){
		console.error(e)
		return res.status(500).send({
			success: false,
			message: "INTERNAL SERVER ERROR",
			data: null
		})
	}
}

const getUserByToken = (req, res) => {
	try {
		const id = req.id

		return res.status(200).send({
			success:true,
			message: "STATUS OK",
			data: id
		})
	} catch(e) {
		console.error(e)
		return res.status(500).send({
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
	interlockBetweenLocalAndGoogle,
	getUserByToken
}
