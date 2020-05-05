package fr.aureliejosephine.go4lunch.models;

public class Booking {

    private String rName;
    private String rUrlPic;
    private String uIdr;
    private String uUsername;
    private String uPic;
    private String rDate;

    public Booking(){}

    public Booking(String rName, String rUrlPic, String uIdr, String uUsername, String uPic, String rDate) {
        this.rName = rName;
        this.rUrlPic = rUrlPic;
        this.uIdr = uIdr;
        this.uUsername = uUsername;
        this.uPic = uPic;
        this.rDate = rDate;
    }


    public String getrName() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }

    public String getrUrlPic() {
        return rUrlPic;
    }

    public void setrUrlPic(String rUrlPic) {
        this.rUrlPic = rUrlPic;
    }

    public String getuIdr() {
        return uIdr;
    }

    public void setuIdr(String uIdr) {
        this.uIdr = uIdr;
    }

    public String getuUsername() {
        return uUsername;
    }

    public void setuUsername(String uUsername) {
        this.uUsername = uUsername;
    }

    public String getuPic() {
        return uPic;
    }

    public void setuPic(String uPic) {
        this.uPic = uPic;
    }

    public String getrDate() {
        return rDate;
    }

    public void setrDate(String rDate) {
        this.rDate = rDate;
    }
}
