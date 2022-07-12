const { Router } = require('express')
const ctrl = require('./ctrl')
const router = Router()
const authUtil = require('./middlewares').checkToken


router.get('/logout', authUtil, ctrl.logout)
router.get('/user-list', authUtil, ctrl.getUserList)
router.get('/Info/:id', authUtil, ctrl.getUserInfo)
router.post('/register', ctrl.register)
router.post('/login', ctrl.login)
router.post('/google/login', authUtil, ctrl.getUserByGoogleToken)
router.post('/google/interlock', authUtil, ctrl.interlockBetweenLocalAndGoogle)
router.post('/token', authUtil, ctrl.getUserByToken)

module.exports = router
