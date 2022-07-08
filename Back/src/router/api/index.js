const { Router } = require('express')
const ctrl = require('./ctrl')
const auth = require('./auth')

const router = Router()

router.get('/', ctrl.pageMain)
router.use('/auth', auth)

module.exports = router

