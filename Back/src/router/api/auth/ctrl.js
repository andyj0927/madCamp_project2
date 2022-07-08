const { Users } = require('../../../lib/database/models')

// POST Register
const register = async (req, res) => {
	console.log(req.body)
	const {userName, displayName, password} = req.body

	try {
		const user = await Users.findOne({
			where: {
				userName: userName,
				displayName: displayName
			}
		})
		console.log(user)
		if(user == undefined || user == null) {
			const newUser = {
				userName: userName,
				displayName: displayName,
				password: password
			}

			await Users.create(newUser)
			res.status(200).send({
				sessionId: req.session.sessionId,
				data: newUser
			})
		}
		else {
			res.status(400).send("Exists id or displayName")
		}
		
	} catch(e){
		res.status(500).send("Internal Server error")
	}
}

// POST Login
const login = async (req, res) => {
	console.log(req.body)
	const {userName, password} = req.body

	try {
		const user = await Users.findOne({
			where:{
				userName:userName
			}
		})

		if(user.password === password) {
			req.session = {
				user
			}; 
			
			res.status(200).send({
				data: user
			})
		}

		else{
			res.status(400).send("login failed")
		}

	} catch (e) {
		console.error(e)
		res.status(500).send("internalServerError")
	}
}

// GET Logout
const logout = (req, res) => {
	try {
		req.session.destroy(e => {
			if(e) throw new Error("BAD_REQUEST")
			res.redirect('/api')
		})
	} catch(e) {
		console.log(e)
		res.status(500).send("internal server error")
	}
}

module.exports = {
	register,
	login,
	logout
}
