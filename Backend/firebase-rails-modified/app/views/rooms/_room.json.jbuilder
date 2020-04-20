json.extract! room, :id, :name, :created_at, :updated_at
json.url user_house_room_url(@user,@house,room, format: :json)