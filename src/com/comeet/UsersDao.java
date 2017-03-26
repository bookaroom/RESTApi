package com.comeet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.core.service.folder.CalendarFolder;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.property.complex.Attendee;
import microsoft.exchange.webservices.data.property.complex.AttendeeCollection;
import microsoft.exchange.webservices.data.search.CalendarView;
import microsoft.exchange.webservices.data.search.FindItemsResults;



public class UsersDao {
    private List<String> getApptAttendees(Appointment appt) throws ServiceLocalException {
        List<String> myAttendees = new ArrayList<String>();
        AttendeeCollection ac = appt.getOptionalAttendees();
        for (Attendee a : ac) {
            myAttendees.add(a.getAddress());
        }

        ac = appt.getRequiredAttendees();
        for (Attendee a : ac) {
            myAttendees.add(a.getAddress());

        }

        return myAttendees;
    }


    /**
     * Gets meetings.
     * @param start Start of query range.
     * @param end End of query range
     * @return List of meetings.
     * @throws Exception If something went wrong.
     */
    public List<Meeting> getUserMeetings(String start, String end) throws Exception {

        ExchangeService service = (new ExchangeConnection()).getService();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd*HH:mm:ss");
        Date startDate = formatter.parse(start);// "2010-05-01 12:00:00");
        Date endDate = formatter.parse(end); // "2010-05-30 13:00:00");
        CalendarFolder cf = CalendarFolder.bind(service, WellKnownFolderName.Calendar);
        FindItemsResults<Appointment> findResults =
                        cf.findAppointments(new CalendarView(startDate, endDate));

        List<Meeting> meetings = new ArrayList<Meeting>();

        for (Appointment appt : findResults.getItems()) {
            if (appt != null) {
                Meeting m = new Meeting();

                m.setStartTime(appt.getStart());
                m.setDuration(appt.getDuration().MINUTES / 1000);
                m.setTitle(appt.getSubject());
                m.setMessage("");// appt.getBody().toString());
                m.setLocation(appt.getLocation());


                m.setAttendees(getApptAttendees(appt));

                meetings.add(m);
            }

        }

        return meetings;
    }



}
