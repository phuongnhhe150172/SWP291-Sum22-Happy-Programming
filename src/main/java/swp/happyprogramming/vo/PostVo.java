package swp.happyprogramming.vo;


public class PostVo {
    private static final long serialVersionUID = 1L;
    private String avatar;
    private String fullName;
    private String description;
    private Integer status;
    private Float price;
    private String methodName;


    public PostVo(String avatar, String fullName, String description, Integer status, Float price, String methodName) {
        this.avatar = avatar;
        this.fullName = fullName;
        this.description = description;
        this.status = status;
        this.price = price;
        this.methodName = methodName;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Float getPrice() {
        return this.price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    

    @Override
    public String toString() {
        return "{" +
            " avatar='" + getAvatar() + "'" +
            ", fullName='" + getFullName() + "'" +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            ", price='" + getPrice() + "'" +
            ", methodName='" + getMethodName() + "'" +
            "}";
    }
   
}
