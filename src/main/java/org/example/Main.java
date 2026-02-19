package org.example;

import org.example.models.Meeting;
import org.example.models.MeetingRoom;
import org.example.models.User;
import org.example.notifications.NotificationService;
import org.example.services.MeetingScheduler;
import org.example.strategy.LeastTimeSpillageStrategy;
import org.example.strategy.RoomSelectionStrategy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {
        // 1. Setup Rooms
        List<MeetingRoom> rooms = new ArrayList<>();
        MeetingRoom roomA = new MeetingRoom("1", "Room A (Cap: 5)", 5);
        MeetingRoom roomB = new MeetingRoom("2", "Room B (Cap: 10)", 10);
        MeetingRoom roomC = new MeetingRoom("3", "Room C (Cap: 10)", 10);
        rooms.add(roomA);
        rooms.add(roomB);
        rooms.add(roomC);

        // 2. Setup Scheduler & Strategy
        RoomSelectionStrategy strategy = new LeastTimeSpillageStrategy();
        MeetingScheduler scheduler = new MeetingScheduler(rooms, strategy);
        scheduler.addObserver(new NotificationService());

        // 3. Setup Users
        List<User> team = List.of(new User("Alice", "alice@test.com"), new User("Bob", "bob@test.com"));

        LocalDateTime today10AM = LocalDateTime.now().withHour(10).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime today11AM = today10AM.plusHours(1);

        // --- TEST 1: Spillage Logic ---
        // We artificially book Room B for 2 hours earlier in the day.
        // Now, Room B has LESS free time than Room C.
        // If we ask for a 10-person room, it should pick Room B to "minimize spillage" of totally empty rooms.
        System.out.println("--- TEST 1: Spillage Logic ---");
        roomB.getCalendar().add(new Meeting(today10AM.minusHours(3), today10AM.minusHours(1), team, roomB));

        // This should pick Room B!
        scheduler.bookMeeting(today10AM, today11AM, 8, team, "MainThread");

        // --- TEST 2: Concurrency Logic ---
        System.out.println("--- TEST 2: Concurrency/Multithreading ---");
        LocalDateTime today2PM = today10AM.plusHours(4);
        LocalDateTime today3PM = today2PM.plusHours(1);

        // Two threads trying to book the exact same time for 4 people.
        // Both will target Room A (since it's empty and fits 5 people).
        // Only one should succeed, the other should fail gracefully.
        Runnable task1 = () -> scheduler.bookMeeting(today2PM, today3PM, 4, team, "Thread-1");
        Runnable task2 = () -> scheduler.bookMeeting(today2PM, today3PM, 4, team, "Thread-2");

        Thread t1 = new Thread(task1);
        Thread t2 = new Thread(task2);

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}