package app.techsol.smartlock.Models;

public class UserModel {

    String name;
    String email;
    String productkey;
    String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProductkey() {
        return productkey;
    }

    public void setProductkey(String productkey) {
        this.productkey = productkey;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    String address;

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
