class AddColumnShelfIdToBook < ActiveRecord::Migration[5.2]
  def change
    add_column :books, :shelf_id, :integer
  end
end
