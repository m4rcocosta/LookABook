package uni.mobile.mobileapp;

class Userinformation {
    public String name;
    public String surname;
    public String phoneNumber;

    public Userinformation(){
    }

    public Userinformation(String name,String surname, String phoneNumber){
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
    }
    public String getUserName() {
        return name;
    }
    public String getUserSurname() {
        return surname;
    }
    public String getUserPhoneNumber() {
        return phoneNumber;
    }
}
