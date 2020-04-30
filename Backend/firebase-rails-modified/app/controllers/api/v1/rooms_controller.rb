class Api::V1::RoomsController < ApiController
  
  before_action :get_user
  
  before_action :get_house
  before_action :set_room, only: [:show, :edit, :update, :destroy]
  
  # GET /rooms
  # GET /rooms.json
  def index
    rooms = @house.rooms
    render json: {status: 'SUCCESS', message: 'Loaded all rooms', data: rooms}, status: :ok
  end

  # GET all books for a user
  def allRooms
    houses=@user.houses
    rooms= houses.map(&:rooms).flatten() 
    render json: {status: 'SUCCESS', message: 'Loaded all rooms', data: rooms}, status: :ok
  end
  
  # GET /rooms/1
  # GET /rooms/1.json
  def show
    room = @room
    render json: {status: 'SUCCESS', message: 'Loaded room', data:  [room]}, status: :ok
  end
  
  # GET /rooms/new
  # def new
  #   @room = @house.rooms.new
  # end
  
  # GET /rooms/1/edit
  # def edit
  # end
  
  # POST /rooms
  # POST /rooms.json
  def create
    @room = @house.rooms.build(room_params)
    
      if @room.save
        
        render json: {status: 'SUCCESS', message: 'Created room', data: [@room]}, status: :ok
      else
        render json: { json: 'Error in creation'}, status: 400
      end
    
  end
  
  # PATCH/PUT /rooms/1
  # PATCH/PUT /rooms/1.json
  def update
    
    if @room.update(room_params)
      render json: {status: 'SUCCESS', message: 'Updated room', data: [@room]}, status: :ok
    else
      render json: { json: 'Error in update'}, status: 400
    end
    
  end
  
  # DELETE /rooms/1
  # DELETE /rooms/1.json
  def destroy
    if @room.destroy
      render json: {status: 'SUCCESS', message: 'Destroyed room', data: [@room]}, status: :ok
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
  
  
  def set_room
    @room = @house.rooms.find(params[:id])
  end
  
  # Never trust parameters from the scary internet, only allow the white list through.
  def room_params
    params.permit(:name)
  end
end
