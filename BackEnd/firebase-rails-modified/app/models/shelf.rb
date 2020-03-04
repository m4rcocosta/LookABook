class Shelf < ApplicationRecord
    belongs_to :wall
    has_many :books
end
