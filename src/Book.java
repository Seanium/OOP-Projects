public class Book {
    private String type;
    private String id;
    private boolean smeared;
    private String school;
    private boolean allowOut;
    private boolean lentOut;
    private String outPerson;
    private String outSchool;
    private String outPid;

    public Book(String type, String id, String school, boolean allowOut) {
        this.type = type;
        this.id = id;
        this.smeared = false;
        this.school = school;
        this.allowOut = allowOut;
        this.lentOut = false;
        this.outPerson = "";
        this.outSchool = "";
        this.outPid = "";
    }

    public void setOut(boolean lentOut, String outPerson, String outSchool, String outPid) {
        this.lentOut = lentOut;
        this.outPerson = outPerson;
        this.outSchool = outSchool;
        this.outPid = outPid;
    }

    public void setSmeared(boolean smeared) {
        this.smeared = smeared;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public boolean isSmeared() {
        return smeared;
    }

    public String getSchool() {
        return school;
    }

    public boolean isAllowOut() {
        return allowOut;
    }

    public boolean isLentOut() {
        return lentOut;
    }

    public String getOutPerson() {
        return outPerson;
    }

    public String getOutSchool() {
        return outSchool;
    }
}

