class Api::V1::HousesController < ApiController
  before_action :get_user
  
  before_action :set_user, only: [:show, :edit, :update, :destroy]
  before_action :set_house, only: [:show, :edit, :update, :destroy]


#new or build ? It s the same?

  # GET /houses
  # GET /houses.json
  def index
    houses = @user.houses
    render json: {status: 'SUCCESS', message: 'Loaded all posts', data: houses}, status: :ok
  end
  
  # GET /houses/1
  # GET /houses/1.json
  def show
    house = @house
    render json: {status: 'SUCCESS', message: 'Loaded all posts', data: house}, status: :ok
  end
  
  # GET /houses/new
  def new
    @house = @user.houses.new
  end
  
  # GET /houses/1/edit
  def edit
  end
  
  # POST /houses
  # POST /houses.json
  def create
    puts @user.houses
    @house = @user.houses.new( house_params)
    @user.houses << @house
    respond_to do |format|
      if @house.save
        
        format.html { redirect_to  user_houses_path(@user), notice: 'House was successfully created.' }
        format.json { render :show, status: :created, location: @house }
      else
        format.html { render :new }
        format.json { render json: @house.errors, status: :unprocessable_entity }
      end
    end
  end
  
  # PATCH/PUT /houses/1
  # PATCH/PUT /houses/1.json
  def update
    respond_to do |format|
      if @house.update(house_params)
        format.html { redirect_to  user_houses_path(@user), notice: 'House was successfully updated.' }
        format.json { render :show, status: :ok, location: @house }
      else
        format.html { render :edit }
        format.json { render json: @house.errors, status: :unprocessable_entity }
      end
    end
  end
  
  # DELETE /houses/1
  # DELETE /houses/1.json
  def destroy
    @house.destroy
    respond_to do |format|
      format.html { redirect_to  user_houses_path(@user), notice: 'House was successfully destroyed.' }
      format.json { head :no_content }
    end
  end
  
  private
  # Use callbacks to share common setup or constraints between actions.
  
  def get_user
    @user = User.find(params[:user_id])
  end
  
  def set_house
    @house = @user.houses.find(params[:id]) #non è nil?
  end
  
  def set_user
    @user = User.find(params[:user_id])
  end

  # Never trust parameters from the scary internet, only allow the white list through.
  def house_params
    params.require(:house).permit(:name, :user_id)
  end
end
