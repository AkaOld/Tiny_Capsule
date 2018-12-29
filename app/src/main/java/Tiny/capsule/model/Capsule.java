package Tiny.capsule.model;

public class Capsule {
    private String name;
    private String host;
    private boolean isPublic;

    public Capsule(String name, String host, boolean isPublic) {
        this.name = name;
        this.host = host;
        this.isPublic = isPublic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}
