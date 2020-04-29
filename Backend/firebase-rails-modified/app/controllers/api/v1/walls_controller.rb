class Api::V1::WallsController < ApiController

  before_action :get_user
  before_action :get_house
  before_action :get_room
  
  before_action :set_wall, only: [:show, :edit, :update, :destroy]

  # GET /walls
  # GET /walls.json
  def index
    walls = @room.walls
    render json: {status: 'SUCCESS', message: 'Loaded all walls', data: walls}, status: :ok
  end

  def allWalls
    houses=@user.houses
    rooms= houses.map(&:rooms).flatten()
    walls = rooms.map(&:walls).flatten()
    render json: {status: 'SUCCESS', message: 'Loaded all walls', data: walls}, status: :ok 
  end

  # GET /walls/1
  # GET /walls/1.json
  def show
    wall = @wall
    render json: {status: 'SUCCESS', message: 'Loaded wall', data: [wall]}, status: :ok
  end

  # GET /walls/new
  # def new
  #   @wall = @room.walls.new
  # end

  # # GET /walls/1/edit
  # def edit
  # end

  # POST /walls
  # POST /walls.json
  def create
    @wall = @room.walls.new(wall_params)

      if @wall.save
        render json: {status: 'SUCCESS', message: 'Created wall', data: [@wall]}, status: :ok
      else
        render json: { json: 'Error in creation'}, status: 400
   end
  end

  # PATCH/PUT /walls/1
  # PATCH/PUT /walls/1.json
  def update
    if @wall.update(wall_params)
    render json: {status: 'SUCCESS', message: 'Updated wall', data: [@wall]}, status: :ok
  else
    render json: { json: 'Error in update'}, status: 400
  end
  end

  # DELETE /walls/1
  # DELETE /walls/1.json
  def destroy
    if @wall.destroy
      render json: {status: 'SUCCESS', message: 'Destroyed wall', data: [@wall]}, status: :ok
    else
      render json: { json: 'Error in destroy'}, status: 400

    end
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    
    def get_user
      @user = User.find(params[:user_id])
    end
    

    def get_house
      if(params[:house_id])
      @house = House.find(params[:house_id])
      end
    end
      
    
    def get_room
      if(params[:room_id])
      @room = Room.find(params[:room_id])
      end
    end
      

    
    def set_wall
      @wall = @room.walls.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def wall_params
      params.permit(:name,:user_id,:room_id,:house_id)
    end
end
