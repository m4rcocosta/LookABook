class RemoveSurnameFromUsers < ActiveRecord::Migration[5.2]
  def change
    remove_column :users, :surname
  end
end
