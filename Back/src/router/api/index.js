const { Router } = require('express')
const ctrl = require('./ctrl')
const auth = require('./auth')

const router = Router()

router.get('/api', ctrl.pageMain)
router.get('/api/auth', auth)

module.exports = router

