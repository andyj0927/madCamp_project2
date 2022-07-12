'use strict';

module.exports = {
  async up (queryInterface, Sequelize) {
    /**
     * Add altering commands here.
     *
     * Example:
     * await queryInterface.createTable('users', { id: Sequelize.INTEGER });
     */
	  await queryInterface.changeColumn('Users', 'userName', {
		  type: Sequelize.STRING(50),
		  allowNull: false,
		  unique: true,
	  })
	  await queryInterface.changeColumn('Users', 'displayName', {
		  type: Sequelize.STRING(50),
		  allowNull: false,
		  unique: true,
	  })
	  await queryInterface.changeColumn('Users', 'password', {
		  type: Sequelize.STRING(50),
		  allowNull: false,
	  })
	  await queryInterface.changeColumn('Users', 'win', {
		  type: Sequelize.INTEGER,
		  allowNull: false,
		  defaultValue: 0,
	  })
	  await queryInterface.changeColumn('Users', 'lose', {
		  type: Sequelize.INTEGER,
		  allowNull: false,
		  defaultValue: 0,
	  })
	  await queryInterface.changeColumn('Users', 'draw', {
		  type: Sequelize.INTEGER,
		  allowNull: false,
		  defaultValue: 0,
	  })
	  await queryInterface.changeColumn('Users', 'total', {
		  type: Sequelize.INTEGER,
		  allowNull: false,
		  defaultValue: 0,
	  })
	  await queryInterface.changeColumn('Users', 'friends', {
		  type: Sequelize.TEXT,
		  allowNull: false,
		  defaultValue: "{}"
	  })
	  await queryInterface.addColumn('Users', 'google', {
		  type: Sequelize.TEXT,
		  allowNull: true,
		  defaultValue: null
	  })
  },

  async down (queryInterface, Sequelize) {
    /**
     * Add reverting commands here.
     *
     * Example:
     * await queryInterface.dropTable('users');
     */
  }
};
