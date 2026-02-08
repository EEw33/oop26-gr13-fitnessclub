package edu.aitu.oop3.components;

public class ConsoleNotificationComponent implements NotificationComponent {

    @Override
    public void notifyUser(String message) {
        System.out.println(message);
    }
}
