class AddHouseToWall < ActiveRecord::Migration[5.2]
  def change
    add_reference :walls, :house, foreign_key: true
  end
end
