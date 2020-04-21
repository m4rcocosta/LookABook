Rails.application.routes.draw do
  resources :users do 
    resources :houses do
      resources :rooms do
        resources :walls do
          resources :shelves do
            resources :books 
          end
        end
      end
    end
  end  
  
  
  namespace :api do 
    namespace :v1 do 
      
      resources :users do 
        resources :houses do
          resources :rooms do
            resources :walls do
              resources :shelves do
                resources :books
              end
            end
          end
        end
      end  
    end 
  end 
  # For details on the DSL available within this file, see http://guides.rubyonrails.org/routing.html
  root 'users#index'
  
  namespace :api do 
    namespace :v1 do 
      
      # Send command to controller to do Google Api analisis   
      get '/users/:user_id/houses/:house_id/rooms/:room_id/walls/:wall_id/shelves/:shelf_id/search-on-google',
      to: 'books#search_on_google', as: "search_on_google"
      
      get '/get-user-by-token',
      to: 'users#get_user_by_token', as: "get_user_by_token"
    end
  end



end
