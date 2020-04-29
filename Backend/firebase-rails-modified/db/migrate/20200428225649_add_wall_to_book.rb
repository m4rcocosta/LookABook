class AddWallToBook < ActiveRecord::Migration[5.2]
  def change
    add_reference :books, :wall, foreign_key: true
  end
end
