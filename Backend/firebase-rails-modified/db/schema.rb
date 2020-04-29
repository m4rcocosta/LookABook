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

ActiveRecord::Schema.define(version: 2020_04_29_014003) do

  # These are extensions that must be enabled in order to support this database
  enable_extension "pgcrypto"
  enable_extension "plpgsql"

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
    t.jsonb "googleData", default: {}, null: false
    t.bigint "wall_id"
    t.bigint "room_id"
    t.bigint "house_id"
    t.bigint "user_id"
    t.index ["house_id"], name: "index_books_on_house_id"
    t.index ["room_id"], name: "index_books_on_room_id"
    t.index ["user_id"], name: "index_books_on_user_id"
    t.index ["wall_id"], name: "index_books_on_wall_id"
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
    t.bigint "user_id"
    t.bigint "house_id"
    t.index ["house_id"], name: "index_houses_users_on_house_id"
    t.index ["user_id"], name: "index_houses_users_on_user_id"
  end

  create_table "rooms", id: :serial, force: :cascade do |t|
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
    t.bigint "room_id"
    t.bigint "house_id"
    t.bigint "user_id"
    t.index ["house_id"], name: "index_shelves_on_house_id"
    t.index ["room_id"], name: "index_shelves_on_room_id"
    t.index ["user_id"], name: "index_shelves_on_user_id"
  end

  create_table "users", id: :serial, force: :cascade do |t|
    t.string "name"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
    t.string "provider_token"
    t.integer "phone"
    t.string "email"
    t.string "auth_token"
    t.index ["auth_token"], name: "index_users_on_auth_token", unique: true
  end

  create_table "walls", force: :cascade do |t|
    t.string "name"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
    t.integer "room_id"
    t.bigint "house_id"
    t.bigint "user_id"
    t.index ["house_id"], name: "index_walls_on_house_id"
    t.index ["user_id"], name: "index_walls_on_user_id"
  end

  add_foreign_key "books", "houses"
  add_foreign_key "books", "rooms"
  add_foreign_key "books", "users"
  add_foreign_key "books", "walls"
  add_foreign_key "houses_users", "houses"
  add_foreign_key "houses_users", "users"
  add_foreign_key "shelves", "houses"
  add_foreign_key "shelves", "rooms"
  add_foreign_key "shelves", "users"
  add_foreign_key "walls", "houses"
  add_foreign_key "walls", "users"
end
