import java.io.Serializable;
import java.io.Serial;

public class Member implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    // Private attributes (Encapsulation)
    private String fullName;
    private String phoneNumber;
    private String address;
    private String uniqueId;

    //Constructor
    public Member(String fullName, String phoneNumber, String address, String uniqueId) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.uniqueId = uniqueId;
    }
    //Getters
    public String getFullName() {
        return fullName;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getAddress() {
        return address;
    }
    public String getUniqueId() {
        return uniqueId;
    }
    //Setters
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

}
