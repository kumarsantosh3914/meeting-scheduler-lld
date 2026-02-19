## Design Meeting Scheduler (LLD)

### **Requirements**:
- There are n given meeting rooms.
- Book a meeting in any meeting room at the given interval (start time, end time, and capacity)
- We have to assign a room to the meeting if available, considering:
   - The room must be free during the requested time.
   -  The room must have at least the required capacity.
- We must minimise spillage of free time (i.e., use the room that has the least free time that can
  accommodate the meeting).
- Send a notification to all the persons who are invited to the meeting
- User meeting Room Calendar to track the meeting date and time (current day)
- Good to have: We have to store audit logs for each room (when a meeting is scheduled, etc.) 
  and delete audit logs after X days.
- Additional considerations: Concurrency - multiple requests may come in simultaneously.


