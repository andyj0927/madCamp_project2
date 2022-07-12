const { Router } = require('express')
const ctrl = require('./ctrl')
const router = Router()

router.post('/sign', ctrl.setJwt)

module.exports = router
