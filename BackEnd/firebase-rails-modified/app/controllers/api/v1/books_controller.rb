class Api::V1::BooksController < ApiController
  
  
  before_action :get_user
  before_action :get_house
  before_action :get_room
  before_action :get_wall
  before_action :get_shelf
  
  
  before_action :set_book, only: [:show, :edit, :update, :destroy]
  
  # GET /books
  # GET /books.json
  def index
    books = @shelf.books
    render json: {status: 'SUCCESS', message: 'Loaded all books', data: books}, status: :ok
  end
  
  # GET /books/1
  # GET /books/1.json
  def show
    book=@book
    render json: {status: 'SUCCESS', message: 'Loaded all book', data: book}, status: :ok
  end
  
  # # GET /books/new
  # def new
  #   @book = @wall.books.build
  # end
  
  # # GET /books/1/edit
  # def edit
  # end
  
  # POST /books
  # POST /books.json
  def create
    @book = @wall.books.build(book_params)
    
    if @book.save
      
      render json: {status: 'SUCCESS', message: 'Created book', data: @book}, status: :ok
    else
      render error: { error: 'Error in creation'}, status: 400
    end
  end
  
  # PATCH/PUT /books/1
  # PATCH/PUT /books/1.json
  def update
    if @book.update(book_params)
      
      render json: {status: 'SUCCESS', message: 'Updated book', data: @book}, status: :ok
    else
      render error: { error: 'Error in update'}, status: 400
    end
  end
  
  # DELETE /books/1
  # DELETE /books/1.json
  def destroy
    if @book.destroy
      
      render json: {status: 'SUCCESS', message: 'Destroyed book', data: @book}, status: :ok
    else
      render error: { error: 'Error in destroy'}, status: 400
    end
  end

  #POST /books-send
  def books_send
    books = @shelf.books
    
    Books::BooksSend.call(book_params) do |m|
      m.success do
        render json: {status: 'API-SUCCESS', message: 'ANALYZED all books', data: books}, status: :ok
      end
      m.failure do |failure|
        render json: {status: 'API-FAIL', message: 'ANALYZED all books', data: books}, status: :ok

      end
    end

    render json: {status: 'SUCCESS', message: 'ANALYZED all books', data: books}, status: :ok
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
    params.permit(:title, :authors, :publisher, :publishedDate, :description,
      :isbn, :pageCount, :categories, :imageLinks, :country, :price)
    end
  end
  