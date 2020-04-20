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
      
      render json: {status: 'SUCCESS', message: 'Created book', data: [@book]}, status: :ok
    else
      render error: { error: 'Error in creation'}, status: 400
    end
  end
  
  # PATCH/PUT /books/1
  # PATCH/PUT /books/1.json
  def update
    if @book.update(book_params)
      
      render json: {status: 'SUCCESS', message: 'Updated book', data: [@book]}, status: :ok
    else
      render error: { error: 'Error in update'}, status: 400
    end
  end
  
  # DELETE /books/1
  # DELETE /books/1.json
  def destroy
    if @book.destroy
      
      render json: {status: 'SUCCESS', message: 'Destroyed book', data: [@book] }, status: :ok
    else
      render error: { error: 'Error in destroy'}, status: 400
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


  
  
  private
  
  def google_search(book)
    puts "Analyzing #{book.title} from thread"
    url = "https://www.googleapis.com/books/v1/volumes?q="  
    url = url + book.title 
    url = URI.encode(url)
    response = HTTParty.get(url, format: :json)
    response = JSON.parse(response.body)["items"][0]
    #response.save("/tmp/search.html")
    puts "Resonse:" + JSON.pretty_generate(response)
    book.googleData = response
    if book.save
      puts ""
    else
      puts "ERROR"
    end
  end
  
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
  