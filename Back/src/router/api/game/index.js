const { Router } = require('express')

const ctrl = require('./ctrl')
const router = Router()

router.post('/game/data-list', ctrl.postGame)

module.exports = router
