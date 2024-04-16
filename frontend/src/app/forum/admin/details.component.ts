import { Component, OnInit, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { AdoptionService } from '../../services/adoption.service';
import { ConfirmedInquiry } from '../../model';

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrl: './details.component.css'
})
export class DetailsComponent implements OnInit {
  private matDialog = inject(MAT_DIALOG_DATA)
  private adoptionSvc = inject(AdoptionService)
  confirmed!: ConfirmedInquiry
  inquiryId!: string


ngOnInit(): void {
    this.inquiryId = this.matDialog.inquiryId
    console.info("i am in dialog",this.inquiryId)
    this.getIndividualAppointment(this.inquiryId)
}

// inquiryId: string 
// dogName: string 
// dogId: string
// url: string
// userFirstName: string
// userLastName: string
// email: string 
// firstTimeOwner: boolean
// intendedVisitDate: string
// nationality: string 
// other: string
// userBirthDate: string
// userId: string
getIndividualAppointment(inquiryId: string ){
  this.adoptionSvc.getIndividualAppointment(inquiryId).then(
  (result)=>{
    console.info(result)
    this.confirmed  =  {
      inquiryId: result.inquiryId,
      userId: result.userId,
      firstName: result.firstName,
      lastName : result.lastName, 
      email: result.email,
      birthdate: result.birthdate,
      nationality: result.nationality,
      other: result.other,
      firstTimeOwner: result.firstTimeOwner,
      intended_visit_date: result.intended_visit_date,
      petId: result.petId,
      dogName: result.dogName,
      url: result.url
    }
    console.info(this.confirmed)

  }
  )
  
}



}
