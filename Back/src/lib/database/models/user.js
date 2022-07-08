const Sequelize = require('sequelize')

class User extends Sequelize.Model {
	static init(sequelize) {
		return super.init({
			userName: {
				type: Sequelize.STRING(50),
				allowNull: false,
				unique: true,
			},
			displayName: {
				type: Sequelize.STRING(50),
				allowNull: false,
				unique: true,
			},
			password: {
				type: Sequelize.STRING(50),
				allowNull: false,
			}
		}, {
			sequelize,
			timestamps: false,
			underscored: false,
			modelName: 'Users',
			tablename: 'users',
			paranoid: false,
			charset: 'utf8',
			collate: 'utf8_general_ci',
		})
	}
}

module.exports = User
