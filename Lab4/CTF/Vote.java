public class Vote {

    private String mValidationNumber;
    private String mIdentificationNumber;
    private String mvoteOption;

    public Vote(String validationNumber, String identificationNumber, String theVoteOption) {
        mValidationNumber = validationNumber;
        mIdentificationNumber = identificationNumber;
        mvoteOption = theVoteOption;
    }

    public String toString() {
        return (mIdentificationNumber + " voted for " + mvoteOption);
    }

    public String getIdentificationNumber() {
        return mIdentificationNumber;
    }
}
