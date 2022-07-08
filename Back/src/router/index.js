const { Router } = require('express')
const api = require('./api')
const ctrl = require('./ctrl')

const router = Router()

router.get('/', ctrl.getUserList)
router.use('/api', api)

module.exports = router
