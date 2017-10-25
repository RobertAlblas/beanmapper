package io.beanmapper.testmodel.superclass;

public class SuperClass {

    public String publicName;

    private String privateName;

    public String getPrivateName() {
        return this.privateName;
    }

    public void setPrivateName(String privateName) {
        this.privateName = privateName;
    }
}
