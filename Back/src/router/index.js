const { Router } = require('express')
const ctrl = require('./ctrl')
const api = require('./api')

const router = Router()

router.get('/', ctrl.redirectToApiMain)
router.get('/api', api)

module.exports = router
