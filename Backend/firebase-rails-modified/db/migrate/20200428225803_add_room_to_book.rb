class AddRoomToBook < ActiveRecord::Migration[5.2]
  def change
    add_reference :books, :room, foreign_key: true
  end
end
