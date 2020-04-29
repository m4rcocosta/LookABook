class ChangeGoogleDataonBook < ActiveRecord::Migration[5.2]
  def change
    change_column_default :books, :googleData, from: "{}", to: {} 
  end
end
