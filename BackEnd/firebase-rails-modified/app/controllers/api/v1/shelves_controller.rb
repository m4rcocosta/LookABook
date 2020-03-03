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
    render json: {status: 'SUCCESS', message: 'Loaded all posts', data: shelves}, status: :ok
  end
  
  # GET /shelves/1
  # GET /shelves/1.json
  def show
    shelf = @shelf
    render json: {status: 'SUCCESS', message: 'Loaded all posts', data: shelf}, status: :ok
  end
  
  # GET /shelves/new
  def new
    @shelf = @wall.shelves.build
  end
  
  # GET /shelves/1/edit
  def edit
  end
  
  # POST /shelves
  # POST /shelves.json
  def create
    @shelf = @wall.shelves.build(shelf_params)
    
    respond_to do |format|
      if @shelf.save
        format.html { redirect_to user_house_room_wall_shelf(:user_id,
        :house_id,:room_id,:wall_id,@shelf),
           notice: 'Shelf was successfully created.' }
        format.json { render :show, status: :created, location: @shelf }
      else
        format.html { render :new }
        format.json { render json: @shelf.errors, status: :unprocessable_entity }
      end
    end
  end
  
  # PATCH/PUT /shelves/1
  # PATCH/PUT /shelves/1.json
  def update
    respond_to do |format|
      if @shelf.update(shelf_params)
        format.html { redirect_to user_house_room_wall_shelf(:user_id,
          :house_id,:room_id,:wall_id,@shelf),
          notice: 'Shelf was successfully updated.' }
        format.json { render :show, status: :ok, location: @shelf }
      else
        format.html { render :edit }
        format.json { render json: @shelf.errors, status: :unprocessable_entity }
      end
    end
  end
  
  # DELETE /shelves/1
  # DELETE /shelves/1.json
  def destroy
    @shelf.destroy
    respond_to do |format|
      format.html { redirect_to user_house_room_wall_shelves(:user_id,
        :house_id,:room_id,:wall_id),
         notice: 'Shelf was successfully destroyed.' }
      format.json { head :no_content }
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
    params.require(:shelf).permit(:name,:user_id,:house_id,:room_id,:wall_id)
  end
end
