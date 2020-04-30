class ChangeForeignKeys < ActiveRecord::Migration[5.2]
  def change
#remove old
    remove_foreign_key "books", "users"
    remove_foreign_key "shelves", "users"
    remove_foreign_key "walls", "users"

    # add the new foreign_key
    add_foreign_key "books", "users", on_delete: :cascade
    add_foreign_key "shelves", "users", on_delete: :cascade
    add_foreign_key "walls", "users", on_delete: :cascade
  
  
  end
end
