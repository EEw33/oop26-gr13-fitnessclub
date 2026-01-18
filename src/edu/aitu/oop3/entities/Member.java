package edu.aitu.oop3.entities;

public class Member {
    private long id;
    private String name;
    private String email;
    private Long membershipTypeId;
    private String membershipStart;
    private String membershipEnd;

    public Member() {
    }

    public Member(long id, String name, String email,
                  Long membershipTypeId,
                  String membershipStart,
                  String membershipEnd) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.membershipTypeId = membershipTypeId;
        this.membershipStart = membershipStart;
        this.membershipEnd = membershipEnd;
    }




    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
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
    public Long getMembershipTypeId() {
        return membershipTypeId;
    }
    public void setMembershipTypeId(Long membershipTypeId) {
        this.membershipTypeId = membershipTypeId;
    }
    public String getMembershipStart() {
        return membershipStart;
    }
    public void setMembershipStart(String membershipStart) {
        this.membershipStart = membershipStart;
    }
    public String getMembershipEnd() {
        return membershipEnd;
    }
    public void setMembershipEnd(String membershipEnd) {
        this.membershipEnd = membershipEnd;
    }
}
