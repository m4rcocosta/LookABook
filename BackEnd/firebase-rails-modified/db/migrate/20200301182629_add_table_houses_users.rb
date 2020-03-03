class AddTableHousesUsers < ActiveRecord::Migration[5.2]
  def change
    create_table :houses_users, id: false do |t|
      t.references :user, index: true, foreign_key: true
      t.references :house, index: true, foreign_key: true
      
    end
  end
end
