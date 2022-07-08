const checkLoggedIn = (req, res, next) => {
	try {
		if(req.session.user == null) {
			res.status(101).send("Need to Login")
		}
		else {
			next()
		}
	} catch(e){
		console.error(e)
	}
}

module.exports = {
	checkLoggedIn,
}
