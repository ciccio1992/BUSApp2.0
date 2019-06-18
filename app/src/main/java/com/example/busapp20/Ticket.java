package com.example.busapp20;

import java.sql.Date;

public class Ticket {
    boolean ticketvalid;
    Date databought;

    /// Constructor method for a ticket
    public void Ticket(boolean Ticketvalid, Date CurrentDate) {
        ticketvalid = Ticketvalid;
        databought = CurrentDate;
    }
}



