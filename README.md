# lookABookApp
Project for Mobile Application and Cloud Computing course


## Pushing rails app on heroku
You need to launch the command:
'''
git subtree push --prefix Backend/firebase-rails-modified heroku master
'''

## Other
'''
git push origin `git subtree split --prefix build gh-pages`:master --force

'''
## To suppress ruby 2.7.1 warnings
'''
RUBYOPT='-W:no-deprecated -W:no-experimental' rails c
'''
or
'''
RUBYOPT='-W:no-deprecated -W:no-experimental' rails s -b 0.0.0.0
'''
git push heroku `git subtree split --prefix Backend/firebase-rails-modified/ master`:master --force

https://lookabookreal.herokuapp.com/

# Heroku setup
npm install -g heroku
heroku login
heroku git:remote -a my-app
