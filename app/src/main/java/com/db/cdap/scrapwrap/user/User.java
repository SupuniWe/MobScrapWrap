package com.db.cdap.scrapwrap.user;

public class User {

    private String userGameName;
    private String userSecurityQuestion1;
    private String userAnswer1;
    private String userSecurityQuestion2;
    private String userAnswer2;
    private String userDeviceId;
    private String userRewards;
    private String userlastPlayed;
    private String userHighestReward;
    private String userEventsAided;
    private String userEventsParticipated;
    private String userEventsPlanned;

    public User()
    {

    }

    public User(String userGameName, String userQues1, String userAns1, String userQues2, String userAns2, String userDevice, String userRewards, String userlastPlayed, String userHighestReward, String userEventsAided, String userEventsParticipated, String userEventsPlanned)
    {
        this.userGameName = userGameName;
        this.userSecurityQuestion1 = userQues1;
        this.userAnswer1 = userAns1;
        this.userSecurityQuestion2 = userQues2;
        this.userAnswer2 = userAns2;
        this.userDeviceId = userDevice;
        this.userRewards = userRewards;
        this.userlastPlayed = userlastPlayed;
        this.userHighestReward = userHighestReward;
        this.userEventsAided = userEventsAided;
        this.userEventsParticipated = userEventsParticipated;
        this.userEventsPlanned = userEventsPlanned;
    }


    public String getUserGameName() {
        return userGameName;
    }

    public void setUserGameName(String userGameName) {
        this.userGameName = userGameName;
    }

    public String getUserSecurityQuestion1() {
        return userSecurityQuestion1;
    }

    public void setUserSecurityQuestion1(String userSecurityQuestion1) {
        this.userSecurityQuestion1 = userSecurityQuestion1;
    }

    public String getUserAnswer1() {
        return userAnswer1;
    }

    public void setUserAnswer1(String userAnswer1) {
        this.userAnswer1 = userAnswer1;
    }

    public String getUserSecurityQuestion2() {
        return userSecurityQuestion2;
    }

    public void setUserSecurityQuestion2(String userSecurityQuestion2) {
        this.userSecurityQuestion2 = userSecurityQuestion2;
    }

    public String getUserAnswer2() {
        return userAnswer2;
    }

    public void setUserAnswer2(String userAnswer2) {
        this.userAnswer2 = userAnswer2;
    }

    public String getUserDeviceId() {
        return userDeviceId;
    }

    public void setUserDeviceId(String userDeviceId) {
        this.userDeviceId = userDeviceId;
    }

    public String getUserRewards() {
        return userRewards;
    }

    public void setUserRewards(String userRewards) {
        this.userRewards = userRewards;
    }

    public String getUserlastPlayed() {
        return userlastPlayed;
    }

    public void setUserlastPlayed(String userlastPlayed) {
        this.userlastPlayed = userlastPlayed;
    }

    public String getUserHighestReward() {
        return userHighestReward;
    }

    public void setUserHighestReward(String userHighestReward) {
        this.userHighestReward = userHighestReward;
    }

    public String getUserEventsAided() {
        return userEventsAided;
    }

    public void setUserEventsAided(String userEventsAided) {
        this.userEventsAided = userEventsAided;
    }

    public String getUserEventsParticipated() {
        return userEventsParticipated;
    }

    public void setUserEventsParticipated(String userEventsParticipated) {
        this.userEventsParticipated = userEventsParticipated;
    }

    public String getUserEventsPlanned() {
        return userEventsPlanned;
    }

    public void setUserEventsPlanned(String userEventsPlanned) {
        this.userEventsPlanned = userEventsPlanned;
    }
}
