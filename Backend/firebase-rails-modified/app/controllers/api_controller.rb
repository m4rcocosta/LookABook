#app/controllers/api_controller.rb

class ApiController < ApplicationController 
    
    before_action :authenticate_user
    
    
    skip_before_action :verify_authenticity_token
    



    private
    def authenticate_user
        user_token = request.headers['TOKEN']
        
        #text = "Current number is: #{request.headers.inspect}"
        #puts text
        
        puts "From headers"
        puts user_token
        if user_token
            puts "validating user"
            @user = User.find_by(auth_token: user_token )
            #Unauthorize if a user object is not returned
            if @user.nil? && !params[:email]
                return unauthorize_wrong_token
            else
                #Unathorize if user id not matching with user token
                puts request.fullpath
                if params[:id]
                    @req_user=User.find_by(params[:id])
                    if user_token.to_s != @req_user.auth_token.to_s  
                        return unauthorize_wrong_token
                    end
                end
            end
        end
    end
    
    def unauthorize_wrong_token
        render json: {error:"Unauthorized access, your token is not valid"}, status: 401
        return false
    end

    def unauthorize_no_token
        render json: {error:"Unauthorized access, you need to pass your token"}, status: 401
        return false
    end

    def person_params
        params.require(:user).permit(:name)
    end

end

