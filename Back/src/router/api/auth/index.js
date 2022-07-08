const { Router } = require('express')
const ctrl = require('./ctrl')

const router = Router()

router.get('/logout', ctrl.logout)
router.post('/register', ctrl.register)
router.post('/login', ctrl.login)

module.exports = router
