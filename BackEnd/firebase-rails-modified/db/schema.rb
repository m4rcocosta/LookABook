# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended that you check this file into your version control system.

ActiveRecord::Schema.define(version: 2020_03_02_181458) do

  create_table "books", force: :cascade do |t|
    t.string "title"
    t.string "authors"
    t.string "publisher"
    t.date "publishedDate"
    t.text "description"
    t.integer "isbn"
    t.integer "pageCount"
    t.string "categories"
    t.string "imageLinks"
    t.string "country"
    t.decimal "price"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
    t.integer "shelf_id"
  end

  create_table "houses", force: :cascade do |t|
    t.string "name"
    t.decimal "latitude"
    t.decimal "longitude"
    t.boolean "isMainHouse"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
  end

  create_table "houses_users", id: false, force: :cascade do |t|
    t.integer "user_id"
    t.integer "house_id"
    t.index ["house_id"], name: "index_houses_users_on_house_id"
    t.index ["user_id"], name: "index_houses_users_on_user_id"
  end

  create_table "rooms", force: :cascade do |t|
    t.string "name"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
    t.string "join_users"
    t.integer "house_id"
    t.integer "user_id"
  end

  create_table "shelves", force: :cascade do |t|
    t.string "name"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
    t.integer "wall_id"
  end

  create_table "users", force: :cascade do |t|
    t.string "name"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
    t.string "provider_token"
    t.string "surname"
    t.integer "phone"
    t.string "email"
  end

  create_table "walls", force: :cascade do |t|
    t.string "name"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
    t.integer "room_id"
  end

end
