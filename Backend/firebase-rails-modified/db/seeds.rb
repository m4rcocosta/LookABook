# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rails db:seed command (or created alongside the database with db:setup).
#
# Examples:
#
#   movies = Movie.create([{ name: 'Star Wars' }, { name: 'Lord of the Rings' }])
#   Character.create(name: 'Luke', movie: movies.first)


Room.create(name: 'my_room', created_at: Time.now, updated_at: Time.now)


5.times do

    user=User.create({
        name: Faker::Name.name,
        phone: Faker::PhoneNumber.cell_phone,
        email: Faker::Internet.email
        })
    user.save

    2.times do
        h= House.create({
            name: Faker::Address.street_name 
        })
        2.times do 
            r = Room.create({name: Faker::House.room })
            2.times do 
                w = Wall.create({name: Faker::Address.building_number})
                2.times do
                    s = Shelf.create({name: Faker::House.furniture })
                    2.times do
                        b = Book.create({title: Faker::Book.title})
                        b.save
                        s.books << b
                    end
                    s.save
                    w.shelves << s
                end
                w.save
                r.walls << w
            end
            r.save
            h.rooms << r
        end
        h.save
        user.houses << h
    end
    user.save

        # HOUSE Faker::Address.street_name 
        # ROOM       Faker::House.room #=> "kitchen"
        # Wall Faker::Address.building_number 
        #SHELF Faker::House.furniture
        # BOOK Faker::Book.title
end