public class Person {

    String mName; 
    String mSocialSecurityNumber;

    public Person(String Name, String socialSecurityNumber) {
        this.mName = Name;
        this.mSocialSecurityNumber = socialSecurityNumber;
    }

    public String getName() {
        return mName;
    }

    public String getSocialSecurityNumber() {
        return mSocialSecurityNumber;
    }

    public String toString() {
        return (mName + " " + String.valueOf(mSocialSecurityNumber));
    }

    public boolean equals(Person p) {
        return(mName.equals(p.mName) && mSocialSecurityNumber.equals(p.mSocialSecurityNumber));
    }
}
