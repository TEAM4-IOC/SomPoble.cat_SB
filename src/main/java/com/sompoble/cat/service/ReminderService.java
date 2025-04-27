package com.sompoble.cat.service;



public interface ReminderService {

    /**
     * Envía recordatorios automáticos para reservas próximas.
     * Este método se ejecuta de forma programada (ej.: diariamente a las 8:00 AM).
     */
    void sendDailyReminders();
    
    void processReminders();
}