package app.techsol.smartlock.Models;

public class UserModel {

    String name, email, productkey, phone, address;

    public UserModel(String name, String email, String productkey, String phone, String address) {
        this.name = name;
        this.email = email;
        this.productkey = productkey;
        this.phone = phone;
        this.address = address;
    }

    public UserModel() {
    }

}
