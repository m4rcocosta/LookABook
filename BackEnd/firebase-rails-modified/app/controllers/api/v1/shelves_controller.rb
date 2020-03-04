class Api::V1::ShelvesController < ApiController
  
  before_action :get_user
  before_action :get_house
  before_action :get_room
  before_action :get_wall
  
  before_action :set_shelf, only: [:show, :edit, :update, :destroy]
  
  # GET /shelves
  # GET /shelves.json
  def index
    shelves = @wall.shelves
    render json: {status: 'SUCCESS', message: 'Loaded all shelves', data: shelves}, status: :ok
  end
  
  # GET /shelves/1
  # GET /shelves/1.json
  def show
    shelf = @shelf
    render json: {status: 'SUCCESS', message: 'Loaded shelf', data: shelf}, status: :ok
  end
  
  # # GET /shelves/new
  # def new
  #   @shelf = @wall.shelves.build
  # end
  
  # # GET /shelves/1/edit
  # def edit
  # end
  
  # POST /shelves
  # POST /shelves.json
  def create
    @shelf = @wall.shelves.build(shelf_params)
    
    if @shelf.save
      
      render json: {status: 'SUCCESS', message: 'Created shelf', data: @shelf}, status: :ok
    else
      render error: { error: 'Error in creation'}, status: 400
      
    end
  end
  
  # PATCH/PUT /shelves/1
  # PATCH/PUT /shelves/1.json
  def updated
    
    if @shelf.update(shelf_params)
      
      render json: {status: 'SUCCESS', message: 'Updated shelf', data: @shelf}, status: :ok
    else
      render error: { error: 'Error in update'}, status: 400  
    end
  end
  
  # DELETE /shelves/1
  # DELETE /shelves/1.json
  def destroy
    if @shelf.destroy
      
      render json: {status: 'SUCCESS', message: 'Destroyed shelf', data: @shelf}, status: :ok
    else
      render error: { error: 'Error in destroy'}, status: 400
    end
  end
  
  private
  
  def get_user
    @user = User.find(params[:user_id])
  end
  
  
  def get_house
    @house = House.find(params[:house_id])
  end
  
  
  def get_room
    @room = Room.find(params[:room_id])
  end
  
  def get_wall
    @wall = Wall.find(params[:wall_id])
  end
  
  # Use callbacks to share common setup or constraints between actions.
  def set_shelf
    @shelf = Shelf.find(params[:id])
  end
  
  # Never trust parameters from the scary internet, only allow the white list through.
  def shelf_params
    params.permit(:name)
  end
end
