package edu.aitu.oop3.entities;

public class ClassBooking {
    private int id;
    private int memberId;
    private int classId;

    public ClassBooking(int id, int memberId, int classId) {
        this.id = id;
        this.memberId = memberId;
        this.classId = classId;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getMemberId() {
        return memberId;
    }
    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }
    public int getClassId() {
        return classId;
    }
    public void setClassId(int classId) {
        this.classId = classId;
    }

}
