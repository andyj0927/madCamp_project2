const { Router } = require('express')
const ctrl = require('./ctrl')
const auth = require('./auth')
const game = require('./game')
const router = Router()

router.get('/', ctrl.getUserList)
router.use('/auth', auth)
router.use('/game', game)

module.exports = router

