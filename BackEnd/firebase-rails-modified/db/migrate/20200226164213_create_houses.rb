class CreateHouses < ActiveRecord::Migration[5.2]
  def change
    create_table :houses do |t|
      t.string :name
      t.decimal :latitude
      t.decimal :longitude
      t.boolean :isMainHouse

      t.timestamps
    end
  end
end
