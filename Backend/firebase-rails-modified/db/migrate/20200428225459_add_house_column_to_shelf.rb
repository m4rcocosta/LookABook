class AddHouseColumnToShelf < ActiveRecord::Migration[5.2]
  def change
    add_reference :shelves, :house, foreign_key: true
  end
end
