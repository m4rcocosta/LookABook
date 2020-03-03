class AddColumnRoomIdToWall < ActiveRecord::Migration[5.2]
  def change
    add_column :walls, :room_id, :integer
  end
end
