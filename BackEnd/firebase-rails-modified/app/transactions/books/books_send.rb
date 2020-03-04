class Books::BooksSend < BaseTransaction
    tee :params
    step :google_request
    #here new steps

    
    def params(input)
        @params = input.fetch(:params)
    end
    
    def google_request(input)
        @lead = Lead.create(@params)
        
        if @lead.errors.any?
            Failure(error: @lead.errors.full_messages.join(' | '))
        else
            Success(input)
        end
    end
    
    
end