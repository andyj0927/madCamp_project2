const { Router } = require('express')
const api = require('./api')

const router = Router()

router.get('/')
router.use('/api', api)

module.exports = router
