import { Component, OnInit, ViewChild, inject } from '@angular/core';
import { AdoptionService } from '../../services/adoption.service';
import { Subscription } from 'rxjs';
import { Appointment } from '../../model';
import { CalendarComponent } from './calendar.component';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {

  @ViewChild(CalendarComponent) calendarComponent!: CalendarComponent;


  viewDate: Date = new Date();
  // events: Appointment[] = [
  //   { start: new Date(), end: new Date(), title: 'Sample Event' }
  // ];

  private adoptionSvc = inject(AdoptionService)
  sub$!: Subscription
  inquiry: any[] = []
  userId!: string
  inquiryId!: string
  confirmation$!: Subscription
  email!: string
  appointmentDate!: string
  firstName!: string
  confirmedAppointments: Appointment[] = [];



  ngOnInit(): void {
    this.getAllInquries()
  }


  getAllInquries() {
    this.sub$ = this.adoptionSvc.getInquiry().subscribe({
      next: ((result) => {
        // console.info(result)
        this.inquiry = result

      }),
      error: ((err) => { console.info("unable to retrieve inquiry", err) }),
      complete: () => {
        this.sub$.unsubscribe()
      },
    })
  }

  acceptInquiry(inquiry: any, index: number) {
    this.inquiryId = inquiry.inquiryId
    this.userId = inquiry.userId
    this.firstName = inquiry.firstName
    this.email = inquiry.email
    this.appointmentDate = inquiry.intended_visit_date

    const newAppointment: Appointment = {
      id: inquiry.inquiryId, // Assuming inquiryId can serve as a unique identifier
      title: 'New Appointment',
      startDate: inquiry.intended_visit_date, // Convert date string to Date object
      startTime: inquiry.selectedHour, // For simplicity, assuming it's a one-hour appointment
    };


    // console.info("array>>", this.confirmedAppointments)

    this.adoptionSvc.acceptInquiry(this.inquiryId, this.userId).then((result) => {
      
      this.confirmation$ = this.adoptionSvc.sendEmailConfirmation(this.firstName, this.email, this.appointmentDate).subscribe({
        next: ((response) => {
          // console.info("Confirmaton email sent successfully", response)
          alert(response.success)
          this.inquiry.splice(index, 1)
        }),
        error: ((err) => { console.info("Failure to send confirmation email", err) }),
        complete: () => { this.confirmation$.unsubscribe() }
      })
      //location.reload()
    })
      .catch((err) => { console.info("unable to accept inquiry", err) })
  }




  declineInquiry(inquiry: any) {
    this.inquiryId = inquiry.inquiryId
    this.userId = inquiry.userId
    this.firstName = inquiry.firstName
    this.email = inquiry.email
    this.appointmentDate = inquiry.intended_visit_date

    this.adoptionSvc.declineInquiry(this.inquiryId, this.userId).then((result)=>{
        // console.info("declined", result)

        this.confirmation$ = this.adoptionSvc.sendEmailDeclination(this.firstName, this.email, this.appointmentDate).subscribe({
          next: ((response) => {
            // console.info("Declination email sent successfully", response)
            alert(response.success)
            this.getAllInquries()
            
          }),
          error: ((err) => { console.info("Failure to send confirmation email", err) }),
          complete: () => { this.confirmation$.unsubscribe() }
        })

    })
    .catch((err)=>{console.info("unable to decline inquiry ", err)})
 
  }

}
