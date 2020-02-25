class Notification
    require 'httparty'

    def self.send_notification(headers = {},params = {},notification, device_token)

    #this is our header we will place our server api key

    headers.merge!({"Authorization" => "key=#{FIREBASE_SERVER_API_KEY}", "Content-Type" => "application/json"})

    #Here is the body and here we will pass FIREBASE_NOTIFICATION_URL#####

    params = notification_params(notification,device_token)
    body = JSON.generate(params)
    HTTParty.post(FIREBASE_NOTIFICATION_URL,
    :body => body,
    :headers => headers)

end

def self.notification_params(notification,device_token)
    params = {}
    params[:registration_ids] = device_token

    params[:priority] = "high"
    data = {}
    data[:notification_id] = notregistration_idsification[:id]
    data[:title] = notification[:title]
    data[:description] = notification[:description]

    #if you want to send image or app name ###
    data[:image] = notification[:image]
    data[:app_name] = notification[:app_name]

    params[:data] = data
    params
end



end