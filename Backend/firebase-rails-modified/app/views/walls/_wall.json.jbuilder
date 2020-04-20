json.extract! wall, :id, :name, :created_at, :updated_at
json.url user_house_room_wall_url(@user,@house,@room,wall, format: :json)
