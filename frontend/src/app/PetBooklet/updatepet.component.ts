import { Component, ElementRef, OnInit, ViewChild, inject } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { PetBooklet } from '../services/petbooklet-service';
import { ActivatedRoute } from '@angular/router';
import { LocalStorageService } from '../services/local-storage-service';
import { MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { PetPhotoComponent } from './pet-photo.component';

@Component({
  selector: 'app-updatepet',
  templateUrl: './updatepet.component.html',
  styleUrl: './updatepet.component.css'
})
export class UpdatepetComponent implements OnInit{
  @ViewChild('file') imageFile!: ElementRef;

  updateForm!: FormGroup
  private fb = inject(FormBuilder)
  private petSvc = inject(PetBooklet)
  private route = inject(ActivatedRoute)
  private localStorage = inject(LocalStorageService)
  private dialog = inject(MatDialog)

  
  userId!: any
  petId!: number
  private matDialog = inject(MAT_DIALOG_DATA)


ngOnInit(): void {
    console.info("oninit")
    this.petId = this.matDialog.petId
    this.updateForm = this.createForm()
    // this.petId = this.route.snapshot.params['petId']
    this.retrievePet(this.petId)
}


  createForm(): FormGroup{
    return this.fb.group({
      name: this.fb.control<string>('', [Validators.maxLength(128)]),
      dateOfBirth: this.fb.control<string>((''), [this.pastDateValidator()]),
      dateOfLastVaccination: this.fb.control<string>((''), [this.pastDateValidator()]),
      gender: [''],
      microchipNumber: this.fb.control<string>('', [Validators.minLength(10),Validators.maxLength(15)]),
      breed: this.fb.control<string>('', [Validators.maxLength(64)]),
      comments: this.fb.control<string>(''),
      // image: this.fb.control<string>("")
    })
  }

  microChipNumber!: string 

  retrievePet(petId: number){
    this.petSvc.getIndividualPet(petId).then((result)=>{
      console.info("retrieved result",result)
      this.updateForm.patchValue(result)
   
      
    }).catch((err)=>{
      console.info("unable to retrieve", err)
      
    })
  }

  getUserId(){
    this.userId = this.localStorage.getItem("userid")
    console.info(this.userId)
  }

  
 

  submit(){
    console.info("button pressed")
    console.info(this.updateForm.value)
    this.userId = this.localStorage.getItem("userid")
    this.petSvc.updateFormFields(this.updateForm, this.petId, this.userId).then((result)=>{
      console.info(result)
      alert(result.success)
      this.dialog.closeAll()
     
    })
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
