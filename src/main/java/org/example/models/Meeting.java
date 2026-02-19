package org.example.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Meeting {
    private final String meetingId;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final List<User> participants;
    private final MeetingRoom room;

    public Meeting(LocalDateTime startDate, LocalDateTime endDate, List<User> participants, MeetingRoom room) {
        this.meetingId = UUID.randomUUID().toString();
        this.startDate = startDate;
        this.endDate = endDate;
        this.participants = participants;
        this.room = room;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public MeetingRoom getRoom() {
        return room;
    }
}
