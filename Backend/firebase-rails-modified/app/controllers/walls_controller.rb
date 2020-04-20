class WallsController < ApplicationController

  before_action :get_user
  before_action :get_house
  before_action :get_room
  
  before_action :set_wall, only: [:show, :edit, :update, :destroy]

  # GET /walls
  # GET /walls.json
  def index
    walls = @room.walls
    render json: {status: 'SUCCESS', message: 'Loaded all posts', data: walls}, status: :ok  end

  # GET /walls/1
  # GET /walls/1.json
  def show
    wall=@wall
    render json: {status: 'SUCCESS', message: 'Loaded all posts', data: wall}, status: :ok
  end

  # GET /walls/new
  def new
    @wall = @room.walls.new
  end

  # GET /walls/1/edit
  def edit
  end

  # POST /walls
  # POST /walls.json
  def create
    @wall = @room.walls.new(wall_params)

    respond_to do |format|
      if @wall.save
        format.html { redirect_to user_house_room_walls_path(@user,@house,@room), notice: 'Wall was successfully created.' }
        format.json { render :show, status: :created, location: @wall }
      else
        format.html { render :new }
        format.json { render json: @wall.errors, status: :unprocessable_entity }
      end
    end
  end

  # PATCH/PUT /walls/1
  # PATCH/PUT /walls/1.json
  def update
    respond_to do |format|
      if @wall.update(wall_params)
        format.html { redirect_to user_house_room_walls_path(@user,@house,@room), notice: 'Wall was successfully updated.' }
        format.json { render :show, status: :ok, location: @wall }
      else
        format.html { render :edit }
        format.json { render json: @wall.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /walls/1
  # DELETE /walls/1.json
  def destroy
    @wall.destroy
    respond_to do |format|
      format.html { redirect_to user_house_rooms_path(@user,@house,@room), notice: 'Wall was successfully destroyed.' }
      format.json { head :no_content }
    end
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    
    def get_user
      @user = User.find(params[:user_id])
    end
    

    def get_house
      @house = House.find(params[:house_id])
    end
      
    
    def get_room
      @room = Room.find(params[:room_id])
    end
      

    
    def set_wall
      @wall = @room.walls.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def wall_params
      params.require(:wall).permit(:name,:user_id,:house_id,:room_id)
    end
end
