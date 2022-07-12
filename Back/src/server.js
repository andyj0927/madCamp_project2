require('./env')
const app = require('./app')

const { createServer } = require('http')
const { Server } = require('socket.io')

const { Users } = require('./lib/database/models')
const { socketUser } = require('./lib/database/models')

const httpServer = createServer(app)
const io = new Server(httpServer)

io.on("connection", socket => {
	console.log(`connection ${socket.id}`)

	socket.on("login", async id => {
		console.log(`current id: ${id}`)
		await Users.update ({
			currentlyActive: 1
		}, {
			where: {
				id
			} 
		})
		
		await socketUser.create({
			user_id: id,
			socket_id: socket.id
		})

		socket.emit("login complete", id)
	})
	
	socket.on("Dual Submit", async ({c_id, d_id}) => {
		console.log(`Data recieve ${c_id}:${d_id}`)

		const d_socket_id = (await socketUser.findOne({
			raw:true,
			where: {
				user_id: d_id
			}
		})).socket_id

		console.log(`defender's socket id: ${d_socket_id}`)
		io.to(d_socket_id).emit("Dual receive", c_id)
	})

	socket.on("disconnect", async () => {
		console.log(`disconnected ${socket.id}`)

		const user_id = await socketUser.findOne({
			raw: true,
			where: {
				socket_id: socket.id
			}
		})

		if(user_id == null) {
			socket.emit("User not found")
			return
		}
		await Users.update({
			currentlyActive: 0
		}, {
			where: {
				id: user_id.user_id
			}
		})

		console.log(`delete socket id ${socket.id}`)
		await socketUser.destroy({
			where: {
				socket_id: socket.id
			}
		})
	})

	socket.on("connectRoom", async (c_id, d_id) => {
		console.log(`room 접속`)

		const c_socket_id = (await socketUser.findOne({
			raw: true,
			where: {
				user_id: c_id
			}
		})).socket_id

		const d_socket_id = (await socketUser.findOne({
			raw: true,
			where: {
				user_id: d_id
			}
		})).socket_id

		console.log(`${c_socket_id} : ${d_socket_id}`)
		socket.emit("socketInfo", c_socket_id, d_socket_id)
	})

	socket.on("giveMeAnArray", c_socket_id => {
		console.log(`d request array to ${c_socket_id}`)
		io.to(c_socket_id).emit("requestArray")
	})

	socket.on("arrayIsHere", (d_socket_id, jsonString) => {
		console.log(`c response json array ${jsonString}`)
		io.to(d_socket_id).emit("responseArray", jsonString)
	})

	socket.on("playingGame", (socketId, pos) => {
		console.log(`${pos} is clicked`)
		io.to(socketId).emit("clickedCardPosition", pos)
	})

	socket.on("GameSet", async (c_id, d_id, cIsWin) => {
		console.log(`Game Set.`)
		const cUser = await findUserById(c_id)
		const dUser = await findUserById(d_id)

		await updateGameRate(cUser, cIsWin)
		await updateGameRate(dUser, -1 * cIsWin)
	})
})

const findUserById = async (id) => {
	const user = await Users.findOne({
		raw: true,
		where: {
			id
		}
	})

	return user
}

const updateGameRate = async (user, isWin) => {
	const total = user.total + 1
	if(isWin == 1) {
		const win = user.win + 1
		await Users.update({
			total,
			win
		}, {
			where: {
				id: user.id
			}
		})
	}

	if(isWin == -1) {
		const lose = user.lose + 1
		await Users.update({
			total,
			lose
		}, {
			where: {
				id: user.id
			}
		})
	}

	if(isWin == 0) {
		const draw = user.draw + 1
		await Users.update({
			total,
			draw
		}, {
			where: {
				id: user.id
			}
		})
	}
}

io.on("connect_error", err => {
	console.log(`connect_error due to ${err.message}`)
})


module.exports = httpServer
