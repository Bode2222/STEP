// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

public final class FindMeetingQuery {
    //Returns list of available times when given an event and a meeting request
    public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
        Collection<String> attendees = request.getAttendees();
        long duration = request.getDuration();

        //If there are no attendees
        if(attendees.size() == 0){
            return Arrays.asList(TimeRange.WHOLE_DAY);
        }

        //If the event lasts longer than a day
        if(duration > TimeRange.WHOLE_DAY.duration()){
            return Arrays.asList();
        }

        ArrayList<TimeRange> unavailableTimes = GenerateUnavailableTimes(events, request);
        ArrayList<TimeRange> availableTimes = GenerateAvailableTimes(unavailableTimes, request.getDuration());
        if(availableTimes.size() <= 0){
            unavailableTimes = GenerateUnavailableTimesForMandatoryAttendees(events, request);
            availableTimes = GenerateAvailableTimes(unavailableTimes, request.getDuration());
        }
        return availableTimes;
    }

    private ArrayList<TimeRange> GenerateAvailableTimes(ArrayList<TimeRange> unavailableTimes, long eventDuration){
        ArrayList<TimeRange> availableTimes = new ArrayList<TimeRange>();
        
        if(unavailableTimes.size() < 1){
            availableTimes.add(TimeRange.WHOLE_DAY);
            return availableTimes;
        }

        Collections.sort(unavailableTimes, TimeRange.ORDER_BY_START);
        //Get the time before the first event of the day
        TimeRange freeTime  = TimeRange.fromStartEnd(TimeRange.START_OF_DAY, unavailableTimes.get(0).start(), false);
        if(freeTime.duration() > 0) availableTimes.add(freeTime);

        //Get the times in between events
        int lastEventEndTime = unavailableTimes.get(0).end();
        for(int i = 1; i < unavailableTimes.size(); i++){
            freeTime = TimeRange.fromStartEnd(lastEventEndTime, unavailableTimes.get(i).start(), false);
            if(freeTime.duration() >= eventDuration) availableTimes.add(freeTime);
            lastEventEndTime = (unavailableTimes.get(i).end() > lastEventEndTime) ? unavailableTimes.get(i).end() : lastEventEndTime;
        }

        //Get time after the last event of the day
        freeTime = TimeRange.fromStartEnd(lastEventEndTime, TimeRange.END_OF_DAY, true);
        if(freeTime.duration() >= eventDuration) availableTimes.add(freeTime);

        return availableTimes;
    }

    private ArrayList<TimeRange> GenerateUnavailableTimes(Collection<Event> events, MeetingRequest request){
        ArrayList<TimeRange> unavailableTimes = new ArrayList<TimeRange>();

        Collection<String> attendeesList = request.getAttendees();
        Set<String> attendeesHash = new HashSet<String>();
        for(String s : attendeesList){
            attendeesHash.add(s);
        }

        //Only add timerange to unavailabletimes if the person someone on the event list is part also on the meeting request list
        for(Event e : events){
            for(String s : e.getAttendees()){
                if(attendeesHash.contains(s)){
                    unavailableTimes.add(e.getWhen());
                    break;
                }
            }
        }

        return unavailableTimes;
    }

    private ArrayList<TimeRange> GenerateUnavailableTimesForMandatoryAttendees(Collection<Event> events, MeetingRequest request){
        ArrayList<TimeRange> unavailableTimes = new ArrayList<TimeRange>();
        int numOfMandatoryEmployees = 0;

        Collection<String> attendeesList = request.getAttendees();
        Set<String> attendeesHash = new HashSet<String>();
        for(String s : attendeesList){
            attendeesHash.add(s);
        }

        //Only add timerange to unavailabletimes if the person someone on the event list is part also on the meeting request list
        //Basically check if each attendee in each event is optional before adding his event times to unavailable events
        for(Event e : events){
            Set<Boolean> attendeesOptionalStatuses = e.getAttendeesOptionalStatus();
            //Check if the number of attendees match the number of attendess-statuses
            if(e.getAttendees().size() != attendeesOptionalStatuses.size()){
                throw new IllegalArgumentException("When trying to get unavailavle times for mandotory attendees, the size of the attendees list did not match the size of the list of the attendees optional statuses");
            }
            Iterator<Boolean> it = attendeesOptionalStatuses.iterator();
            for(String s : e.getAttendees()){
                boolean isAttendeeOptional = it.next();
                if(attendeesHash.contains(s) && !isAttendeeOptional){
                    unavailableTimes.add(e.getWhen());
                    numOfMandatoryEmployees++;
                    break;
                }
            }
        }

        //If there are no mandatory employees, return that no time is available
        if(numOfMandatoryEmployees == 0){
            unavailableTimes = new ArrayList<TimeRange>();
            unavailableTimes.add(TimeRange.fromStartEnd(TimeRange.START_OF_DAY, TimeRange.END_OF_DAY, true));
        }
        return unavailableTimes;
    }
}
