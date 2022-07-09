const { Router } = require('express')
const ctrl = require('./ctrl')
const router = Router()
const authUtil = require('./middlewares').checkToken


router.get('/logout', authUtil, ctrl.logout)
router.get('/user-list', authUtil, ctrl.getUserList)
router.post('/register', ctrl.register)
router.post('/login', ctrl.login)
router.post('/google/login', authUtil, ctrl.googleLogin)
router.post('/google/interlock', authUtil, ctrl.interlockGoogle)

module.exports = router
