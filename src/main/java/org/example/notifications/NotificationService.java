package org.example.notifications;

import org.example.models.Meeting;
import org.example.models.User;

public class NotificationService implements MeetingObserver {
    @Override
    public void onMeetingScheduled(Meeting meeting) {
        System.out.println("--- NOTIFICATIONS ---");
        for (User u : meeting.getParticipants()) {
            System.out.println("Email sent to " + u.getEmail() + ": Meeting booked in " +
                    meeting.getRoom().getName() + " from " +
                    meeting.getStartDate().toLocalTime() + " to " + meeting.getEndDate().toLocalTime());
        }
        System.out.println("---------------------\n");
    }
}
