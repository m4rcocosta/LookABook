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
      
      # Send books to controller to do Google Api analisis   
      post '/users/:user_id/houses/:house_id/rooms/:room_id/walls/:wall_id/shelves/:shelf_id/books-send',
      to: 'books#books_send', as: "books_send"
    end
  end



end
