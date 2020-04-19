class AddGoogleDataToBooks < ActiveRecord::Migration[5.2]
  def change
    add_column :books, :googleData, :jsonb, null: false, default:"{}"
  end
end
