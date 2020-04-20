class BooksController < ApplicationController
  
  
  before_action :get_user
  before_action :get_house
  before_action :get_room
  before_action :get_wall
  before_action :get_shelf
  
  
  before_action :set_book, only: [:show, :edit, :update, :destroy]
  
  # GET /books
  # GET /books.json
  def index
    @books = @shelf.books
  end
  
  # GET /books/1
  # GET /books/1.json
  def show
  end
  
  # GET /books/new
  def new
    @book = @wall.books.build
  end
  
  # GET /books/1/edit
  def edit
  end
  
  # POST /books
  # POST /books.json
  def create
    @book = @wall.books.build(book_params)
    
    respond_to do |format|
      if @book.save
        format.html { redirect_to user_house_room_wall_shelf_book(:user_id,
        :house_id,:room_id,:wall_id,:shelf_id,@book),
           notice: 'Book was successfully created.' }
        format.json { render :show, status: :created, location: @book }
      else
        format.html { render :new }
        format.json { render json: @book.errors, status: :unprocessable_entity }
      end
    end
  end
  
  # PATCH/PUT /books/1
  # PATCH/PUT /books/1.json
  def update
    respond_to do |format|
      if @book.update(book_params)
        format.html { redirect_to user_house_room_wall_shelf_book(:user_id,
          :house_id,:room_id,:wall_id,:shelf_id,@book),
          notice: 'Book was successfully updated.' }
        format.json { render :show, status: :ok, location: @book }
      else
        format.html { render :edit }
        format.json { render json: @book.errors, status: :unprocessable_entity }
      end
    end
  end
  
  # DELETE /books/1
  # DELETE /books/1.json
  def destroy
    @book.destroy
    respond_to do |format|
      format.html { redirect_to user_house_room_wall_shelf_books(:user_id,
        :house_id,:room_id,:wall_id,:shelf_id),
         notice: 'Book was successfully destroyed.' }
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
  

  def get_shelf
    @shelf = Shelf.find(params[:shelf_id])
  end

  # Use callbacks to share common setup or constraints between actions.
  def set_book
    @book = Book.find(params[:id])
  end
  
  # Never trust parameters from the scary internet, only allow the white list through.
  def book_params
    params.require(:book).permit(:title, :authors, :publisher, :publishedDate, :description,
      :isbn, :pageCount, :categories, :imageLinks, :country, :price,:user_id,:house_id,
      :room_id,:wall_id,:shelf_id)
    end
  end
  