class RemoveApiTokenFromUser < ActiveRecord::Migration[5.2]
  def change
    remove_column :users, :api_token, :string
  end
end
