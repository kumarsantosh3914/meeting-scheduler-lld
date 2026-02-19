package org.example.strategy;

import org.example.models.Meeting;
import org.example.models.MeetingRoom;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class LeastTimeSpillageStrategy implements RoomSelectionStrategy {
    @Override
    public MeetingRoom selectRoom(List<MeetingRoom> availableRooms, int requiredCapacity, LocalDateTime start, LocalDateTime end) {
        MeetingRoom selectedRoom = null;
        long minimumFreeTime = Long.MAX_VALUE;

        for(MeetingRoom room: availableRooms) {
            // We want to pick the room that is already used the most,
            // leaving completely empty rooms available for larger/longer meetings.
            long freeMinutes = calculateFreeTimeInDay(room);

            if (freeMinutes < minimumFreeTime) {
                minimumFreeTime = freeMinutes;
                selectedRoom = room;
            }
        }

        return selectedRoom;
    }

    private long calculateFreeTimeInDay(MeetingRoom room) {
        long totalMinutesInDay = 24 * 60; // 1440 minutes
        long bookedMinutes = 0;

        for (Meeting m : room.getCalendar()) {
            bookedMinutes += Duration.between(m.getStartDate(), m.getEndDate()).toMinutes();
        }
        return totalMinutesInDay - bookedMinutes;
    }
}
