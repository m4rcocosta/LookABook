  class Api::V1::UsersController < ApiController
    before_action :set_user, only: [:show, :edit, :update, :destroy]

    # GET /users
    # GET /users.json

    # def index
    #   users = User.all
    #   render json: {status: 'SUCCESS', message: 'Loaded all users', data: users}, status: :ok
    # end

    # GET /users/1
    # GET /users/1.json
    def show
      render json: {status: 'SUCCESS', message: 'Loaded user', data: [@user]}, status: :ok
    end

    # GET /users/new
    # def new
    #   @user = User.new
    # end

    # # GET /users/1/edit
    # # def edit
    # end

    # POST /users
    # POST /users.json
    def create
      @user = User.new(user_params)

        if @user.save
          render json: {status: 'SUCCESS', message: 'Created user', data: [@user]}, status: :ok
        else
          render error: { error: 'Error in creation of user'}, status: 400
        end
      
    end

    # PATCH/PUT /users/1
    # PATCH/PUT /users/1.json
    def update
        if @user.update(user_params)
          render json: {status: 'SUCCESS', message: 'Patched user', data: [@user]}, status: :ok
        else
          render error: { error: 'Error in creation of user'}, status: 400

      end
    end

    # DELETE /users/1
    # DELETE /users/1.json
    def destroy
      if @user.destroy
        render json: {status: 'SUCCESS', message: 'Destroyed user', data: [@user]}, status: :ok
      end
    end

    def get_user_by_token
      user=User.find_by(auth_token:request.headers['TOKEN'])
      if user
        render json: {status: 'SUCCESS', message: 'Loaded user', data: [user]}, status: :ok
      else
        render error: { error: 'Error in creation'}, status: 404
      end
    end 

    private
      # Use callbacks to share common setup or constraints between actions.
      def set_user
        @user = User.find(params[:id])
      end

      # Never trust parameters from the scary internet, only allow the white list through.
      def user_params
        params.permit(:name,:auth_token )
      end
  end
