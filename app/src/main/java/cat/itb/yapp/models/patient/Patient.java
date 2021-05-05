package cat.itb.yapp.models.patient;

import java.time.LocalDateTime;

public class Patient {
    private int id;
    private String name;
    private String surname;
    private String phoneNumber;
    private String email;
    private String urlPhoto;
    private LocalDateTime dateOfBirth;
    private int age;
    private String homeAddress;
    private String schoolName;
    private String course;
    private String paymentType;
    private LocalDateTime registerDate;
    private boolean active;

    public Patient(int id, String name, String surname, String phoneNumber, String email, String urlPhoto, LocalDateTime dateOfBirth, int age, String homeAddress, String schoolName, String course, String paymentType, LocalDateTime registerDate, boolean active) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.urlPhoto = urlPhoto;
        this.dateOfBirth = dateOfBirth;
        this.age = age;
        this.homeAddress = homeAddress;
        this.schoolName = schoolName;
        this.course = course;
        this.paymentType = paymentType;
        this.registerDate = registerDate;
        this.active = active;
    }

    public Patient(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public LocalDateTime getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDateTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public LocalDateTime getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDateTime registerDate) {
        this.registerDate = registerDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = true;
    }
}
