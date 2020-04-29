class AddRoomsToShelves < ActiveRecord::Migration[5.2]
  def change
    add_reference :shelves, :room, foreign_key: true
  end
end
