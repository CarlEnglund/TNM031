public class Vote {

    private String hashId;
    private String voteOption;
    private String hashValidationNumber;

    public Vote(String theHashId, String theHashValidationNumber, String theVoteOption) {
        hashId = theHashId;
        hashValidationNumber = theHashValidationNumber;
        voteOption = theVoteOption;
    }

    public String CTFMessage() {
        String msgToCTF = hashId + ":" + hashValidationNumber + ":" + voteOption;
        return msgToCTF;
    }
}
