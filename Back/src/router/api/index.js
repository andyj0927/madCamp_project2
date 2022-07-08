const { Router } = require('express')
const ctrl = require('./ctrl')
const auth = require('./auth')

const router = Router()

router.get('/', ctrl.getUserList)
router.use('/auth', auth)

module.exports = router

