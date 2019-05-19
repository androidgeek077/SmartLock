package app.techsol.smartlock.Models;

public class uploadProduct {
    public uploadProduct(String productkey, String imgurl) {
        this.productkey = productkey;
        this.imgurl = imgurl;
    }

    public String getProductkey() {
        return productkey;
    }

    public void setProductkey(String productkey) {
        this.productkey = productkey;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public uploadProduct() {
    }

    String productkey,imgurl;
}
