package com.example.boardgameapp.modul;

public class GroupCardModule {
    private String groupName;
    private String nextMeeting;
    private String nextResidence;
    public GroupCardModule() {}

    public GroupCardModule(String groupName, String nextMeeting, String nextResidence) {
    this.groupName = groupName;
    this.nextMeeting = nextMeeting;
    this.nextResidence = nextResidence;
}

public String getGroupName() {
    return groupName;
}public String getNextMeeting() {
    return nextMeeting;
}public String getNextResidence() {
    return nextResidence;
}
}
