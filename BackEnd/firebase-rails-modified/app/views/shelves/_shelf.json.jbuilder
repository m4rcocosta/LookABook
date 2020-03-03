json.extract! shelf, :id, :name, :created_at, :updated_at
json.url user_house_room_wall_shelf_url(@user,@house,@room,
    @wall,shelf, format: :json)
