class CreateBooks < ActiveRecord::Migration[5.2]
  def change
    create_table :books do |t|
      t.string :title
      t.string :authors
      t.string :publisher
      t.date :publishedDate
      t.text :description
      t.integer :isbn
      t.integer :pageCount
      t.string :categories
      t.string :imageLinks
      t.string :country
      t.decimal :price

      t.timestamps
    end
  end
end
