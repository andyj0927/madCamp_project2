exports.isLoggedIn = (req, res, next) => {
	if(req.isAuthenticated()) {
		next()
	}
	else{
		res.status(403).send({ message: "Need to Login" })
	}
}

exports.isNotLoggedIn = (req, res, next) => {
	if(!req.isAuthenticated()) {
		next()
	}
	else {
		const message = encodeURIComponent("Already Logged in")
		res.status(400).send({ message: "BAD REQUEST" })
	}
}
