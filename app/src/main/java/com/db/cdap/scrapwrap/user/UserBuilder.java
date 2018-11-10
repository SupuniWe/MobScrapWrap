package com.db.cdap.scrapwrap.user;

public class UserBuilder {

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

    public UserBuilder()
    {

    }

    public User buildUser()
    {
        return new User(userGameName, userSecurityQuestion1, userSecurityQuestion2, userAnswer1, userAnswer2, userDeviceId, userRewards, userlastPlayed, userHighestReward, userEventsAided, userEventsParticipated, userEventsPlanned);
    }

    public UserBuilder name(String name)
    {
        this.userGameName = name;
        return this;
    }

    public UserBuilder question1(String question1)
    {
        this.userSecurityQuestion1 = question1;
        return this;
    }

    public UserBuilder answer1(String answer1)
    {
        this.userAnswer1 = answer1;
        return this;
    }

    public UserBuilder question2(String question2)
    {
        this.userSecurityQuestion2 = question2;
        return this;
    }

    public UserBuilder answer2(String answer2)
    {
        this.userAnswer2 = answer2;
        return this;
    }

    public UserBuilder userDeviceId(String userDeviceId)
    {
        this.userDeviceId = userDeviceId;
        return this;
    }

    public UserBuilder userRewards(String userRewards)
    {
        this.userRewards = userRewards;
        return this;
    }

    public UserBuilder userlastPlayed(String userlastPlayed)
    {
        this.userlastPlayed = userlastPlayed;
        return this;
    }

    public UserBuilder userHighestReward(String userHighestReward)
    {
        this.userHighestReward = userHighestReward;
        return this;
    }

    public UserBuilder userEventsAided(String userEventsAided)
    {
        this.userEventsAided = userEventsAided;
        return this;
    }

    public UserBuilder userEventsParticipated(String userEventsParticipated)
    {
        this.userEventsParticipated = userEventsParticipated;
        return this;
    }

    public UserBuilder userEventsPlanned(String userEventsPauserEventsPlannedrticipated)
    {
        this.userEventsPlanned = userEventsPlanned;
        return this;
    }
}
