'use strict';
module.exports = {
  async up(queryInterface, Sequelize) {
    await queryInterface.createTable('Games', {
      id: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: Sequelize.INTEGER
      },
      c_id: {
        type: Sequelize.INTEGER
      },
      d_id: {
        type: Sequelize.INTEGER
      },
      num_arr: {
        type: Sequelize.TEXT
      },
      c_score: {
        type: Sequelize.INTEGER,
		  allowNull: false,
		  defaultValue: 0
      },
      d_score: {
        type: Sequelize.INTEGER,
		  allowNull: false,
		  defaultValue: 0
      },
      createdAt: {
        allowNull: false,
        type: Sequelize.DATE
      },
      updatedAt: {
        allowNull: false,
        type: Sequelize.DATE
      }
    });
  },
  async down(queryInterface, Sequelize) {
    await queryInterface.dropTable('Games');
  }
};
