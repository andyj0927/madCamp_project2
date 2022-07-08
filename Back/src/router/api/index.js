const { Router } = require('express')
const ctrl = require('./ctrl')
const auth = require('./auth')
const { checkLoggedIn } = require('../../lib/middleware/userMiddleware')

const router = Router()

router.get('/', checkLoggedIn, ctrl.getUserList)
router.use('/auth', auth)

module.exports = router

