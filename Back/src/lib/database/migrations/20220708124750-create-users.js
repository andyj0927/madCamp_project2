'use strict';
module.exports = {
  async up(queryInterface, Sequelize) {
    await queryInterface.createTable('Users', {
      id: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: Sequelize.INTEGER
      },
      userName: {
        type: Sequelize.STRING(50),
		  allowNull: false,
		  unique: true
      },
      displayName: {
        type: Sequelize.STRING(50),
		  allowNull: false,
		  unique: true
      },
      password: {
        type: Sequelize.STRING(50),
		  allowNull: false,
      },
      win: {
        type: Sequelize.INTEGER,
		  allowNull: false,
		  defaultValue: 0
      },
      draw: {
        type: Sequelize.INTEGER,
		  allowNull: false,
		  defaultValue: 0
      },
      lose: {
        type: Sequelize.INTEGER,
		  allowNull: false,
		  defaultValue: 0
      },
      total: {
        type: Sequelize.INTEGER,
		  allowNull: false,
		  defaultValue: 0
      },
      friends: {
		  type: Sequelize.TEXT,
		  allowNull: true
      },
      currentlyActivy: {
		  allowNull: false,
		  defaultValue: 0,
        type: Sequelize.TINYINT
      },
      createdAt: {
        allowNull: false,
        type: Sequelize.DATE
      },
      updatedAt: {
        allowNull: false,
        type: Sequelize.DATE
      }
	}, {
		timestamps: true,
		underscored: false,
		nodemName: 'User',
		tableName: 'Users',
		paranoid: false,
		charset: 'utf8',
		collate: 'utf8_general_ci'
	});
  },
  async down(queryInterface, Sequelize) {
    await queryInterface.dropTable('Users');
  }
};
