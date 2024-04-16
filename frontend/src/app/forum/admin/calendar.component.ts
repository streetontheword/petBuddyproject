import { Component, Input, OnDestroy, inject } from '@angular/core';
import { ChangeDetectorRef } from '@angular/core';
import {
  ChangeDetectionStrategy,
  OnInit,
  ViewEncapsulation,
} from '@angular/core';
import { CalendarEventTitleFormatter, CalendarEvent as AngularCalendarEvent } from 'angular-calendar';
import { WeekViewHourSegment } from 'calendar-utils';
import { addDays, addMinutes, endOfWeek, format, startOfWeek } from 'date-fns';
import { Subscription, fromEvent } from 'rxjs';
import { finalize, first, takeUntil } from 'rxjs/operators';
import { Appointment, MyCalendarEvent } from '../../model';
import { AdoptionService } from '../../services/adoption.service';
import { DatePipe } from '@angular/common';
import { Router } from '@angular/router';
import { CalendarStore } from './CalendarStore';
import { AuthService } from '../../services/auth-service';
import { CalendarService } from '../../services/calendar.service';
import { MatDialog } from '@angular/material/dialog';
import { DetailsComponent } from './details.component';



function floorToNearest(amount: number, precision: number) {
  return Math.floor(amount / precision) * precision;
}


function ceilToNearest(amount: number, precision: number) {
  return Math.ceil(amount / precision) * precision;
}

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrl: './calendar.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [DatePipe,
    {
      provide: CalendarEventTitleFormatter,
    },
  ],
  encapsulation: ViewEncapsulation.None,
})

export class CalendarComponent implements OnInit, OnDestroy {

  confirmedInquiry: any[] = []
  date!: string
  time!: string
  ownerName!: string


  confirmedAppointments: Appointment[] = [];
  sub$!: Subscription

  confirmedAppointmentsSubscription: Subscription | undefined;

  private adoptionSvc = inject(AdoptionService)
  private router = inject(Router)
  viewDate = new Date();
  viewStartHour: number = 8; // Set view start hour to 8 am
  viewStart = 8
  viewEnd = 20
  weekStartsOn: 0 = 0;
  inquiryID!: string

  dragToCreateActive = false;
  events: AngularCalendarEvent[] = [];
  days: any[] = [];
  slots: any[] = [];
  isAdmin!: boolean
  private authSvc = inject(AuthService)
  private calendarSvc = inject(CalendarService)
  constructor(private cdr: ChangeDetectorRef, private datePipe: DatePipe) { }
  private calendarStore = inject(CalendarStore)
  private eventsSubscription!: Subscription


  ngOnInit(): void {

    this.initDays();

    // console.info("i am in calendar component")
    this.isAdmin = this.authSvc.isAdminLoggedIn()
    this.calendarStore.loadCalendarEventsIntoStore() //data is loaded into store and confirmed
    // this.getAppointments()

    this.eventsSubscription = this.calendarStore.events$.subscribe(eventsFromSQL => {
      // console.log("Current events in the store:", eventsFromSQL);
      const data = eventsFromSQL
      this.events = []
      for (const obj of data) {

        const date = obj.intended_visit_date;
        const time = obj.selectedHour;
        //name
        const ownerName = obj.firstName;
        const dateTime = new Date(date);
        const inquiryId = obj.inquiryId
        // console.info("inquiry ID>>", inquiryId)
        const formattedDate = this.datePipe.transform(dateTime, 'shortDate');
        //start
        const combinedDateTimeString = `${formattedDate} ${time}`;

        //end
        const endDateTime = new Date(combinedDateTimeString); // Clone the start date
        endDateTime.setHours(endDateTime.getHours() + 2);
        //  this.cdr.detectChanges
        this.mapAppointmentsToEvents(inquiryId, combinedDateTimeString, endDateTime, ownerName)


      }




    })

  }



  mapAppointmentsToEvents(inquiryId: string, combinedDateTimeString: string, endDateTime: Date, ownerName: string): void {

    this.inquiryID = inquiryId

    const event: any = {
      start: new Date(combinedDateTimeString),
      end: endDateTime,
      title: "Meeting with " + ownerName || 'My Appointment',

      color: {
        primary: '#1e90ff',
        secondary: '#D1E8FF',
      },
      meta: {
        // You can include additional metadata if needed
        inquiryId: inquiryId,

      },

    };
  
    this.events.push(event)
    this.refresh()
 


    // this.cdr.detectChanges();
  }

  private dialog = inject(MatDialog)

  openDialog(inquiryId: string): void {


    const dialogRef = this.dialog.open(DetailsComponent, {
      data: {
        inquiryId: inquiryId
      }
    });

    dialogRef.afterClosed().subscribe(result => {
   

    });
  }

  handleEventClicked({ event }: { event: AngularCalendarEvent<any> }): void {
    // Here you can implement the logic to handle the event click
    // console.log('Event clicked:', event);
    const inquiryId = (event.meta as any).inquiryId;
    // console.log('Inquiry ID:', inquiryId);

    this.openDialog(inquiryId)

    // For example, you can navigate to a different route or display more details about the event
  }


  initDays() {
    this.days = [
      'Sunday',
      'Monday',
      'Tuesday',
      'Wednesday',
      'Thursday',
      'Friday',
      'Saturday',
    ];
    for (let i = 0; i < this.days.length; i++) {
      let a = { day: this.days[i], time: [] };
      this.slots.push(a);
    }
  }



  refresh() {
    this.events = [...this.events];
    this.cdr.detectChanges();
    // this.getSlots();
  }

  convertTime(t: string | number | Date) {
    return new Date(t).toTimeString();
  }

  convertDay(d: string | number | Date) {
    return new Date(d).toLocaleString('en-us', {
      weekday: 'long',
    });
  }



  removeSlot(id: string | number | undefined) {
    for (let j = 0; j < this.slots.length; j++) {
      this.slots[j].time = this.slots[j].time.filter((t: { id: string | number | undefined; }) => t.id !== id);
    }
  }
  goToNextWeek() {
    this.viewDate = addDays(startOfWeek(this.viewDate, { weekStartsOn: this.weekStartsOn }), 7);
    // Refresh events and slots for the new week
    this.refresh();
  }
  goToPreviousWeek() {
    this.viewDate = addDays(startOfWeek(this.viewDate, { weekStartsOn: this.weekStartsOn }), -7);
    // Refresh events and slots for the new week
    this.refresh();
  }

  ngOnDestroy(): void {
    // Unsubscribe from the confirmedAppointments$ observable to avoid memory leaks
    if (this.confirmedAppointmentsSubscription) {
      this.confirmedAppointmentsSubscription.unsubscribe();
    }
  }


}