class AddColumnHouseIdToRoom < ActiveRecord::Migration[5.2]
  def change
    add_column :rooms, :house_id, :integer
  end
end
