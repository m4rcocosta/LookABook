class User < ApplicationRecord
    has_secure_token :auth_token
    
    has_and_belongs_to_many :houses , dependent: :destroy
end
