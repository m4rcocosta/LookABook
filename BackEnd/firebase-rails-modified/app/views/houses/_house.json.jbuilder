json.extract! house, :id, :name, :latitude, :longitude, :isMainHouse, :created_at, :updated_at
json.url user_house_url(@user,house, format: :json)
