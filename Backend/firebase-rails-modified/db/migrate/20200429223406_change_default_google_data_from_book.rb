class ChangeDefaultGoogleDataFromBook < ActiveRecord::Migration[5.2]
  def up
    change_column_default :books, :googleData, {}
    change_column :books, :googleData, :jsonb, null:true
  end

  def down
    change_column_default :books, :googleData, "{}"
    change_column :books, :googleData, :jsonb, null:false
  end
end
