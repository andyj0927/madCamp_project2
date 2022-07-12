const fs = require('fs')
const readline = require('readline')
const { google } = require('googleapis')
const googleClient = require('../../config/google.json')

const SCOPES = ['https://www.googleapis.com/auth/plus.me']
const TOKEN_PATH = 'token_json'

authorize(JSON.parse(googleClient), listConnectionNames)

const authorize = (credentials, callback) => {
	const { client_secret, client_id, redirect_uris } = credentials["installed"]
	const oAuth2Client = new google.auth.OAuth2(

		client_id, client_secret, redirect_uris[0]
	)

	fs.readFile(TOKEN_PATH, (err, token) => {
		if (err) return getNewToken(oAuth2Client, callback)
		oAuth2Client.setCredentials(JSON.parse(token))
		callback(oAuth2Client)
	})
}

const getNewToken = (oAuth2Client, callback) => {
	const authUrl = oAuth2Client.generateAuthUrl({
		access_type: 'offline',
		scope: SCOPES,
	})
	console.log('Authorize this app by visiting this url: ', authUrl)
	const rl = readline.createInterface({
		input: process.stdin,
		output: process.stdout
	})
	rl.question('Enter the code from that page here: ', code => {
		rl.close()
		oAuth2Client.getToken(code, (err, token) => {
			if (err) return console.error('Error retrieving access token', err)
			oAuth2Client.setCredentials(token)
			fs.writeFile(TOKEN_PATH, JSON.stringify(token), err => {
				if (err) return console.error(err)
				consolg.log('Token stored to', TOKEN_PATH)
			})
			callback(oAuth2Client)
		})
	})
}

const listConnectionNames = auth => {
	const service = google.people({
		version: 'v1',
		auth
	})
	service.people.connections.list({
		resourceName: 'poeple/me',
		pageSize: 10,
		personField: 'names,emailAddresses',
	}, (err, res) => {
		if (err) return console.error('The API returned an error: ', err)
		const connections = res.data.connections

		if(connections) {
			console.log('Connections: ')
			connections.forEach(person => {
				if(person.names && person.names.length > 0) {
					console.log(person.names[0].displayName)
				} else {
					console.log('No display name found for connection')
				}
			})
		} else {
			console.log('No connections found.')
		}
	})
}
