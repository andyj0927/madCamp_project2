const { Game } = require('../../../lib/database/models')

const postGame = async (req, res) => {
	try {
		const { c_id, d_id, num_arr, c_score, d_score } = req.body

		const newGame = await Game.create({
			c_id,
			d_id,
			num_arr,
			c_score,
			d_score
		})

		console.log(newGame)
		return res.status(200).send({
			success: true,
			message: "STATUS OK",
			data: newGame
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


module.exports = {
	postGame
}
