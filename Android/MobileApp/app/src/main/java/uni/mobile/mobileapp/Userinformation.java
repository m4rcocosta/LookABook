package uni.mobile.mobileapp;

class Userinformation {
    public String phoneNumber;

    public Userinformation(){
    }

    public Userinformation(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }
    public String getUserPhoneNumber() {
        return phoneNumber;
    }
}
