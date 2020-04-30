#app/controllers/api_controller.rb

class ApiController < ApplicationController 
    
    before_action :authenticate_user
    
    wrap_parameters false 
    #skip_before_action :verify_authenticity_token
    
    
    
    
    private
    def authenticate_user
        user_token = request.headers['TOKEN']
        
        #text = "Current number is: #{request.headers.inspect}"
        #puts text
        
        puts "Api check in... "
        puts "path: "+ request.fullpath
        puts "method: " + request.method
        if(user_token)
            puts "token: ..."+ user_token.last(6)
        else
            puts "token: ... no token param"
        end
        # if user_token
        #     puts "validating user by token"
        #     @user = User.find_by(auth_token: user_token )
        #     #Unauthorize if a user object is not returned
        #     if @user.nil? 
        #         if user_token=="" && request.fullpath.slice(0..request.fullpath.index("?")) != "/api/v1/users/get-user-by-email"
        #             puts "user_token=="" && not user by get mail"
        #             return unauthorize_no_token
        #         elsif request.fullpath != "/api/v1/users" || request.method!="POST"
        #             puts "no user and no create on user"
        #             return unauthorize_wrong_token                
        #         end
        #     else
        #         #Unathorize if user id not matching with user token
        #         if params[:id] 
        #             @req_user=User.find_by(params[:id])
        #             if user_token.to_s != @req_user.auth_token.to_s  
        #                 puts "unmatched token"
        #                 return unauthorize_wrong_token
        #             end
        #         else
        #             if (request.fullpath != "/api/v1/users" && request.method!="POST")
        #                 puts "no param id and no create"
        #                 return no_param_id
        #             end
        #         end
        #     end    
        # else 
        #     puts "no token param"
        #     return unauthorize_no_token
        # end
        
        
        if ! user_token
            puts request.fullpath[0,request.fullpath.index('?')]
            if(request.fullpath[0,request.fullpath.index('?')] != "/api/v1/users/get-user-by-email")
                puts "ok è una get email stai calmo"
            else
                return no_param_token
            end 
        else #Token param c'è
            if user_token == ""
                if(request.fullpath[0,request.fullpath.index('?')] != "/api/v1/users/get-user-by-email")
                    puts "ok è una get email stai calmo"
                else
                    puts "not token and not get email"
                    return unauthorize_no_token
                end 
            else
                @user = User.find_by(auth_token: user_token ) 
                if @user.nil? &&  (request.fullpath != "/api/v1/users" || request.method!="POST")#Non esiste token e non è una post
                    puts "no user found "
                    return unauthorize_no_token
                elsif params[:user_id] #User con quel token esiste
                    @req_user=User.find(params[:user_id])
                    if user_token.to_s != @req_user.auth_token.to_s  
                        puts "unmatched token"
                        return unauthorize_wrong_token
                    end
                    puts "[V] token and user matching"
                elsif params[:id] #User con quel token esiste
                    @req_user=User.find(params[:id])
                    if user_token.to_s != @req_user.auth_token.to_s  
                        puts "unmatched token"
                        return unauthorize_wrong_token
                    end
                    puts "[V] token and user matching"
                
                end
            end
            
        end
        
        
        
    end
    
    def unauthorize_wrong_token
        puts "Unauthorized access, your token is not valid"
        render json: {error:"Unauthorized access, your token is not valid"}, status: 401
        return false
    end
    
    def unauthorize_no_token
        puts "Unauthorized access, you need to pass your token"
        render json: {error:"Unauthorized access, you need to pass your token"}, status: 401
        return false
    end
    
    def unauthorize_no_token_no_mail
        puts "No token and no mail"
        render json: {error:"Unauthorized access, you need to pass your token or correct email"}, status: 401
        return false
    end
    
    
    def no_param_token
        puts "No token param in this request"
        render json: {error:"No token param in this request"}, status: 401
        return false
    end
    
    def no_param_id
        puts "No user id param in this request"
        render json: {error:"No user id param in this request"}, status: 401
        return false
    end
    
    
    
end

