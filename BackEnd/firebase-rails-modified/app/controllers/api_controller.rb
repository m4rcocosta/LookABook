#app/controllers/api_controller.rb
class ApiController < ApplicationController 
    skip_before_action :verify_authenticity_token
end