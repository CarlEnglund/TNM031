public class Person {

    String mName; 
    String mPersonalNumber;

    public Person(String Name, String PersonalNumber) {
        this.mName = Name;
        this.mPersonalNumber = PersonalNumber;
    }

    public String getName() {
        return mName;
    }

    public String getPersonalNumber() {
        return mPersonalNumber;
    }

    public String toString() {
        return (mName + " " + String.valueOf(mPersonalNumber));
    }

    public boolean equals(Person p) {
        return(mName.equals(p.mName) && mPersonalNumber == p.mPersonalNumber);
    }
}
