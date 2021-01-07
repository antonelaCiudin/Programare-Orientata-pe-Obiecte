class Pasager {
    private String id;
    private String name;
    private int age;
    private char tipe_ticket;
    private Boolean priority_embark;
    private Boolean special_needs;
    private int priority;
    int idx;

    public Pasager(String id, String name, int age, char tipe_ticket,
                   Boolean priority_embark, Boolean special_needs) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.tipe_ticket = tipe_ticket;
        this.priority_embark = priority_embark;
        this.special_needs = special_needs;
    }

    public String getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public char getTipe_ticket() {
        return tipe_ticket;
    }

    public Boolean getPriority_embark() {
        return priority_embark;
    }

    public Boolean getSpecial_needs() {
        return special_needs;
    }

    public int getPriority() {
        return priority;
    }

    public String getName() { return name; }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
