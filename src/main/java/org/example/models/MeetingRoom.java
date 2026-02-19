package org.example.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class MeetingRoom {
    private final String roomId;
    private final String name;
    private final int capacity;
    private final List<Meeting> calendar;

    // Lock for handling concurrent booking requests for this specific room
    private final ReentrantLock lock;

    public MeetingRoom(String roomId, String name, int capacity) {
        this.roomId = roomId;
        this.name = name;
        this.capacity = capacity;
        this.calendar = new ArrayList<>();
        this.lock = new ReentrantLock();
    }

    public boolean isAvailable(LocalDateTime startDate, LocalDateTime endDate) {
        for(Meeting m: calendar) {
            if (startDate.isBefore(m.getEndDate()) && endDate.isAfter(m.getStartDate())) {
                return false; // Time conflict
            }
        }

        return true;
    }

    public boolean bookRoom(Meeting meeting) {
        lock.lock();
        try {
            // Double-check availability inside the lock (Crucial for concurrency)
            if (isAvailable(meeting.getStartDate(), meeting.getEndDate())) {
                calendar.add(meeting);
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    public int getCapacity() {
        return capacity;
    }

    public List<Meeting> getCalendar() {
        return calendar;
    }

    public String getName() {
        return name;
    }
}
