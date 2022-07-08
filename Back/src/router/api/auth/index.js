const { Router } = require('express')
const ctrl = require('./ctrl')
const { isLoggedIn, isNotLoggedIn } = require('./middlewares')
const router = Router()

router.get('/logout', isLoggedIn, ctrl.logout)
router.get('/user-list/get', ctrl.getUserList)
router.post('/register', isNotLoggedIn, ctrl.register)
router.post('/login', isNotLoggedIn, ctrl.login)

module.exports = router
