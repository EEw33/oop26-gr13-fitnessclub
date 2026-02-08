package edu.aitu.oop3.monitoring;

public class ConsoleNotificationComponent implements NotificationComponent {

    @Override
    public void notifyUser(String message) {
        System.out.println(message);
    }
}
