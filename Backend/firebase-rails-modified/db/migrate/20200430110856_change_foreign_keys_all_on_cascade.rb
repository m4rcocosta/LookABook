class ChangeForeignKeysAllOnCascade < ActiveRecord::Migration[5.2]
  def change
    remove_foreign_key "books", "houses"
    remove_foreign_key "books", "rooms"
    remove_foreign_key "books", "walls"
    remove_foreign_key "shelves", "houses"
    remove_foreign_key "shelves", "rooms"
    remove_foreign_key "walls", "houses"
    
    add_foreign_key "books", "houses", on_delete: :cascade
    add_foreign_key "books", "rooms", on_delete: :cascade
    add_foreign_key "books", "walls", on_delete: :cascade
    add_foreign_key "shelves", "houses", on_delete: :cascade
    add_foreign_key "shelves", "rooms", on_delete: :cascade
    add_foreign_key "walls", "houses", on_delete: :cascade
    
    
    
    
  end
end
