class House < ApplicationRecord
    has_and_belongs_to_many :users
    has_many :rooms
    accepts_nested_attributes_for :rooms
end
