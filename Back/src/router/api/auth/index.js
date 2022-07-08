const { Router } = require('express')
const ctrl = require('./ctrl')

const router = Router()

router.post('/auth/register', ctrl.register)

module.exports = router;
