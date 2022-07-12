'use strict';
const {
  Model
} = require('sequelize');
module.exports = (sequelize, DataTypes) => {
  class Tempgame extends Model {
    /**
     * Helper method for defining associations.
     * This method is not a part of Sequelize lifecycle.
     * The `models/index` file will call this method automatically.
     */
    static associate(models) {
      // define association here
    }
  }
  Tempgame.init({
    c_id: DataTypes.INTEGER,
    d_id: DataTypes.INTEGER
  }, {
    sequelize,
    modelName: 'Tempgame',
  });
  return Tempgame;
};