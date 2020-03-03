class AddColumnWallIdToShelf < ActiveRecord::Migration[5.2]
  def change
    add_column :shelves, :wall_id, :integer
  end
end
