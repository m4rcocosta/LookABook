class Shelf < ApplicationRecord
    belongs_to :wall
    has_many :books

    accepts_nested_attributes_for :books
  
end
