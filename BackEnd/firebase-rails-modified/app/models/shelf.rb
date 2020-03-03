class Shelf < ApplicationRecord
    belongs_to :walls
    has_many :books
end
