import { Injectable, inject } from '@angular/core';
import { ComponentStore } from '@ngrx/component-store';
import { MyCalendarEvent } from '../../model';
import { CalendarService } from '../../services/calendar.service';


export interface CalendarEventsSlice {
  events: MyCalendarEvent[];
}

const INIT_STATE: CalendarEventsSlice = {
  events: [],
};

@Injectable({
  providedIn: 'root',
})
export class CalendarStore extends ComponentStore<CalendarEventsSlice> {
  private calendarSvc = inject(CalendarService)

  constructor() { super(INIT_STATE); }

  readonly events$ = this.select((state) => state.events);

  readonly setEvents = this.updater((state, events: MyCalendarEvent[]) => ({
    ...state,
    events,
  }));

  readonly updateEvents = this.updater((state, newEvent: MyCalendarEvent) => ({
    ...state,
    events: [...state.events, newEvent]
  }));


  readonly getAllThreads = this.select<MyCalendarEvent[]>(
    (slice: CalendarEventsSlice) => slice.events

  )


  readonly loadToStore = this.updater<MyCalendarEvent[]>(
    (_slice: CalendarEventsSlice, values: MyCalendarEvent[]) => {
      return {
        events: values
      } as CalendarEventsSlice
    }
  )

  loadCalendarEventsIntoStore() {
    this.calendarSvc.getConfirmedInquiry().subscribe((events: MyCalendarEvent[]) => {
      console.info("instore>>>", events)
      this.loadToStore(events);
      console.info("logging events into component store")
    });

  }
}


// readonly loadToStore = this.updater<MyCalendarEvent[]>(
//   (state, events) => ({
//     ...state,
//     events: events
//   })
// );

// loadCalendarEventsIntoStore() {
//   this.calendarSvc.getConfirmedInquiry().subscribe({
//     next: (events: MyCalendarEvent[]) => {
//       console.info("Logging events into component store:", events);
//       this.loadToStore(events);
//     },
//     error: (error) => {
//       console.error("Error loading calendar events:", error);
//     }
//   });
// }



