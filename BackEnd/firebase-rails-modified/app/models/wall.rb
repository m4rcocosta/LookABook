class Wall < ApplicationRecord

  belongs_to :room
  has_many :shelves
  
  after_commit :sync_create

    def self.get_wall(wall_id)
      FIREBASE_DB.get('/walls/' + wall_id.to_s)
    end
  
    def sync_create
      add_wall
    end
  
    private
  
    def add_wall
      FIREBASE_DB.post('/walls/' + id.to_s, {
        name: name
      })
    end
end
