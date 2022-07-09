const { Router } = require('express')
const ctrl = require('./ctrl')
const router = Router()

router.get('/', ctrl.getJwt)

module.exports = router
