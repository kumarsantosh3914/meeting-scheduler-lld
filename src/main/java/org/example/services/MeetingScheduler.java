package org.example.services;

import org.example.models.Meeting;
import org.example.models.MeetingRoom;
import org.example.models.User;
import org.example.notifications.MeetingObserver;
import org.example.strategy.RoomSelectionStrategy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MeetingScheduler {
    private List<MeetingRoom> allRooms;
    private RoomSelectionStrategy selectionStrategy;
    private List<MeetingObserver> observers = new ArrayList<>();

    public MeetingScheduler(List<MeetingRoom> rooms, RoomSelectionStrategy selectionStrategy) {
        this.allRooms = rooms;
        this.selectionStrategy = selectionStrategy;
    }

    public void addObserver(MeetingObserver observer) {
        this.observers.add(observer);
    }

    public synchronized Meeting bookMeeting(LocalDateTime start, LocalDateTime end, int capacity, List<User> participatns, String bookedByThread) {
        System.out.println("[" + bookedByThread + "] Attempting to book meeting for " + capacity + " people...");

        // 1. Filter rooms that are currently available for the given time slot
        List<MeetingRoom> availableRooms = new ArrayList<>();
        for(MeetingRoom room: allRooms) {
            if(room.isAvailable(start, end)) {
                availableRooms.add(room);
            }
        }

        // 2. Apply Strategy to find the optimal room
        MeetingRoom bestRoom = selectionStrategy.selectRoom(availableRooms, capacity, start, end);

        if(bestRoom == null) {
            System.out.println("[" + bookedByThread + "] FAILED: No suitable rooms available.");
            return null;
        }

        // 3. Create the meeting and attempt to book (handles concurrency lock)
        Meeting meeting = new Meeting(start, end, participatns, bestRoom);

        boolean isBooked = bestRoom.bookRoom(meeting);

        if (isBooked) {
            System.out.println("[" + bookedByThread + "] SUCCESS: Room " + bestRoom.getName() + " booked.");
            // 4. Notify Observers
            for (MeetingObserver obs : observers) {
                obs.onMeetingScheduled(meeting);
            }
            return meeting;
        } else {
            System.out.println("[" + bookedByThread + "] FAILED: Concurrency conflict! Room grabbed by another user.");
            return null;
        }
    }
}
