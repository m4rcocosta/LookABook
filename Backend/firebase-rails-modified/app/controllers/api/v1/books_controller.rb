#require 'net/http'

require 'async'
require 'async/http/internet'
require 'uri'
require 'httparty'


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
    render json: {status: 'SUCCESS', message: 'Loaded all book', data: [book]}, status: :ok
  end
  
  # GET all books for a user
  def allBooks
    houses=@user.houses
    rooms= houses.map(&:rooms).flatten() 
    walls=rooms.map(&:walls).flatten()
    shelves=walls.map(&:shelves).flatten()
    books=shelves.map(&:books).flatten()
    render json: {status: 'SUCCESS', message: 'Loaded all books', data: books}, status: :ok
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
    @book = @shelf.books.build(book_params)
    
    if @book.save
      
      render json: {status: 'SUCCESS', message: 'Created book', data: [@book]}, status: :ok
    else
      render json: { json: 'Error in creation'}, status: 400
    end
  end
  
  # PATCH/PUT /books/1
  # PATCH/PUT /books/1.json
  def update
    if @book.update(book_params)
      render json: {status: 'SUCCESS', message: 'Updated book', data: [@book]}, status: :ok
    else
      render json: { json: 'Error in update'}, status: 400
    end
  end
  
  # DELETE /books/1
  # DELETE /books/1.json
  def destroy
    if @book.destroy
      
      render json: {status: 'SUCCESS', message: 'Destroyed book', data: [@book] }, status: :ok
    else
      render json: { json: 'Error in destroy'}, status: 400
    end
  end
  
  #GET /search-on-google
  def search_on_google
    books = @shelf.books
    puts "<<< Starting Async search"
    my_threads = []
    books.each do |b|
      puts "Starting thread for #{b.title}"
      my_threads << Thread.new{ google_search(b)} 
    end
    
    puts "Async search started >>>"
    render json: {status: 'SUCCESS', message: 'Search on Google request received', data: books}, status: :ok
  end
  
  #GET /scanAllBooks
  def scanAllBooks
    houses=@user.houses
    rooms= houses.map(&:rooms).flatten() 
    walls = rooms.map(&:walls).flatten()
    shelves = walls.map(&:shelves).flatten()
    books = shelves.map(&:books).flatten()
    puts "<<< Starting Async search"
    my_threads = []
    books.each do |b|
      puts "Starting thread for #{b.title}"
      my_threads << Thread.new{ google_search(b)} 
    end
    
    puts "Async search started >>>"
    render json: {status: 'SUCCESS', message: 'Search on Google request received', data: books}, status: :ok
  end
  
  
  
  private
  
  def google_search(book)
    puts "Analyzing #{book.title} from thread"
    url = "https://www.googleapis.com/books/v1/volumes?q="  
    url = url + book.title 
    url = URI.encode(url)
    response = HTTParty.get(url, format: :json)
    response = JSON.parse(response.body)["items"][0]
    
    #response.save("/tmp/search.html")
    #puts "Resonse:" + JSON.pretty_generate(response)+"..."
    book.googleData = response
    if book.save
     # puts "[V] Book #{book.title} #{book.googleData.last(10)} updated"
      puts "[V] Book #{book.title}  updated"
    else
      if room.errors.any?
        puts "[E] #{ book.errors.full_messages }"
      end
    end
  end
  
  def get_user
    if(params[:user_id])
      @user = User.find(params[:user_id])
    end
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
  
  def get_wall
    if(params[:wall_id])
      @wall = Wall.find(params[:wall_id])
    end
  end
  
  
  def get_shelf
    if(params[:shelf_id])
      @shelf = Shelf.find(params[:shelf_id])
    end
  end
  
  # Use callbacks to share common setup or constraints between actions.
  def set_book
    if(params[:id])
      @book = Book.find(params[:id])
    end
  end
  
  # Never trust parameters from the scary internet, only allow the white list through.
  def book_params
    params.permit(:title, :authors, :publisher, :publishedDate, :description,
    :isbn, :pageCount, :categories, :imageLinks, :country, :price,
    :user_id,:house_id,:room_id,:wall_id,:shelf_id,
    :googleData,:small_thumbnail)
  end
end
