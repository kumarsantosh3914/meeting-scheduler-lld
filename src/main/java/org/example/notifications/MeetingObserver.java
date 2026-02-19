package org.example.notifications;

import org.example.models.Meeting;

public interface MeetingObserver {
    void onMeetingScheduled(Meeting meeting);
}
