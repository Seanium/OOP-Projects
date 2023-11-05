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
    private String state;
    private int limitDay;

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
        this.state = "Normal";
        this.limitDay = 0;
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

    public void refuseLend() {
        //System.out.println("(State) [YYYY-mm-dd] <类别号-序列号> transfers from <原状态> to <新状态>\n");
        String oldState = state;
        state = oldState;
        System.out.printf("(State) %s %s-%s transfers from %s to %s\n",
                Controller.getDate(), type, id, oldState, state);
    }

    public void lend() {
        //System.out.println("(State) [YYYY-mm-dd] <类别号-序列号> transfers from <原状态> to <新状态>\n");
        String oldState = state;
        state = "Lent";
        System.out.printf("(State) %s %s-%s transfers from %s to %s\n",
                Controller.getDate(), type, id, oldState, state);
    }

    public void collect() {
        //System.out.println("(State) [YYYY-mm-dd] <类别号-序列号> transfers from <原状态> to <新状态>\n");
        String oldState = state;
        state = "Normal";
        System.out.printf("(State) %s %s-%s transfers from %s to %s\n",
                Controller.getDate(), type, id, oldState, state);
    }

    public void repair() {
        //System.out.println("(State) [YYYY-mm-dd] <类别号-序列号> transfers from <原状态> to <新状态>\n");
        String oldState = state;
        state = oldState;
        System.out.printf("(State) %s %s-%s transfers from %s to %s\n",
                Controller.getDate(), type, id, oldState, state);
    }

    public void receive() {
        //System.out.println("(State) [YYYY-mm-dd] <类别号-序列号> transfers from <原状态> to <新状态>\n");
        String oldState = state;
        state = oldState;
        System.out.printf("(State) %s %s-%s transfers from %s to %s\n",
                Controller.getDate(), type, id, oldState, state);
    }

    public void transport() {
        //System.out.println("(State) [YYYY-mm-dd] <类别号-序列号> transfers from <原状态> to <新状态>\n");
        String oldState = state;
        state = oldState;
        System.out.printf("(State) %s %s-%s transfers from %s to %s\n",
                Controller.getDate(), type, id, oldState, state);
    }

    public void setLimitDay(String date) {
        this.limitDay = type.equals("B") ? Date.stringDateToDays(date) + 30 :
                Date.stringDateToDays(date) + 60;
    }

    public int getLimitDay() {
        return limitDay;
    }
}

