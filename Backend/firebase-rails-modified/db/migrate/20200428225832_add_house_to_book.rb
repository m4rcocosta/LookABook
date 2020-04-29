class AddHouseToBook < ActiveRecord::Migration[5.2]
  def change
    add_reference :books, :house, foreign_key: true
  end
end
