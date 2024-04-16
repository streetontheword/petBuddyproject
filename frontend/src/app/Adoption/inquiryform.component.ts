import { Component, Inject, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormControl, ValidatorFn, AbstractControl } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { AdoptionService } from '../services/adoption.service';
import { LocalStorageService } from '../services/local-storage-service';
import { Subscription, first } from 'rxjs';
import { MatSelectModule } from '@angular/material/select';




@Component({
  selector: 'app-inquiryform',
  templateUrl: './inquiryform.component.html',
  styleUrl: './inquiryform.component.css'
})
export class InquiryformComponent implements OnInit {

  hours: string[] = ['9:00 AM', '10:00 AM', '11:00 AM', '12:00 PM', '1:00 PM', '2:00 PM', '3:00 PM', '4:00 PM', '5:00 PM', '6:00 PM', '7:00 PM', '8:00 PM'];
  // selectedHourControl = new FormControl();

  inquiryForm!: FormGroup

  private fb = inject(FormBuilder)
  private dialog = inject(MatDialog)
  private adoptionSvc = inject(AdoptionService)
  private localStorage = inject(LocalStorageService)
  private matDialog = inject(MAT_DIALOG_DATA)


  userid!: any
  email!: string
  appointmentDate!: string
  firstName!: string
  petid!: number
  url!: string
  petName!: string
  inquiry$!: Subscription

  ngOnInit(): void {

    this.petid = this.matDialog.petId
    this.url = this.matDialog.url[0]
    this.petName = this.matDialog.dogName


    this.inquiryForm = this.createForm()
  }

  createForm(): FormGroup {
    return this.fb.group({
      firstName: this.fb.control<string>('', [Validators.required, Validators.maxLength(128)]),
      lastName: this.fb.control<string>('', [Validators.required, Validators.maxLength(128)]),
      email: this.fb.control<string>('', [Validators.required, Validators.email]),
      nationality: this.fb.control<string>(''),
      other: this.fb.control<string>('', [Validators.maxLength(128)]),
      dateOfViewing: this.fb.control<string>((''), [Validators.required, this.futureDateValidator()]),
      dateOfBirth: this.fb.control<string>((''), [Validators.required, this.pastDateValidator()]),
      gender: this.fb.control<string>(''),
      firstTimeOwner: this.fb.control<boolean>(false),
      selectedHour: this.fb.control<string>('', [Validators.required])


    })
  }



  processForm() {
    this.userid = this.localStorage.getItem("userid")
    console.info("button was pressed")
    let value: any = this.inquiryForm.value
    console.info(value)
    this.firstName = this.inquiryForm.value['firstName']
    this.email = this.inquiryForm.value['email']
    this.appointmentDate = this.inquiryForm.value['dateOfViewing']


    console.info(this.petid)

    this.adoptionSvc.makeInquiry(this.userid, this.petid, this.url, this.petName, this.inquiryForm).then(
      (result) => {
        console.info(result)
        alert("Inquiry sent!")

        //send confirmation email 
        this.inquiry$ = this.adoptionSvc.sendEmail(this.firstName, this.email, this.appointmentDate).subscribe({
          next: ((response) => {
            console.info("Confirmaton email sent successfully", response)
          }),
          error: ((err) => { console.info("Failure to send confirmation email", err) }),
          complete: () => { this.inquiry$.unsubscribe() }
        })
      }).catch((err) => { console.info("cannot send inquiry to backend", err) })
    this.dialog.closeAll()
  }


  futureDateValidator(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      const inputDate = new Date(control.value);
      const currentDate = new Date();
      currentDate.setHours(0, 0, 0, 0);  // Remove time parts to only compare dates

      // Check if the input date is greater than today's date
      return inputDate > currentDate ? null : { 'notFutureDate': true };
    };
  }


  pastDateValidator(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      const today = new Date();
      today.setHours(0, 0, 0, 0); // Reset time to 00:00:00 for accurate comparison
      const inputDate = new Date(control.value);

      return inputDate < today ? null : { 'pastDate': { value: control.value } };
    };
  }
}


