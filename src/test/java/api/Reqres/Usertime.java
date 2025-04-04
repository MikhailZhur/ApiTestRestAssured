package api.Reqres;

public class Usertime {
    private String name;
    private String job;

    public Usertime() {
    }

    public Usertime(String name, String job) {
        this.name = name;
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }
}
