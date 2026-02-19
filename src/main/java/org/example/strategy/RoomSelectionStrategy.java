package org.example.strategy;

import org.example.models.MeetingRoom;

import java.time.LocalDateTime;
import java.util.List;

public interface RoomSelectionStrategy {
    MeetingRoom selectRoom(List<MeetingRoom> availableRooms, int requiredCapacity, LocalDateTime start, LocalDateTime end);
}
