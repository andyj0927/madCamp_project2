const getCurrentUser = (req, res) => {
	try {
		res.status(200).send({sessionId: req.session.sessionId})
	} catch (e) {
		res.status(500).send("Internal Server Error")
	}
}

module.exports = {
	getCurrentUser,
}
