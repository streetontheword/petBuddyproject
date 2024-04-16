import { Component, ElementRef, OnInit, ViewChild, inject } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { PetBooklet } from '../services/petbooklet-service';
import { LocalStorageService } from '../services/local-storage-service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-petbooklet',
  templateUrl: './petbooklet.component.html',
  styleUrl: './petbooklet.component.css'
})
export class PetbookletComponent implements OnInit {

  @ViewChild('file') imageFile!: ElementRef;


  form!: FormGroup
  userId!: any
  username!: any


  private fb = inject(FormBuilder)
  private petBookletSvc = inject(PetBooklet)
  private localStorage = inject(LocalStorageService)
  private router = inject(Router)



  ngOnInit(): void {
    this.form = this.createForm()
    this.getUserId();
    this.username = this.localStorage.getItem("username")

  }


  createForm(): FormGroup {
    return this.fb.group({
      name: this.fb.control<string>('', [Validators.required,Validators.maxLength(128)]),
      dateOfBirth: this.fb.control<string>((''),[this.pastDateValidator()]),
      dateOfVaccination: this.fb.control<string>((''), [this.pastDateValidator()]),
      gender: this.fb.control<string>((''), [Validators.required]),
      microChipNumber: this.fb.control<string>('', [Validators.minLength(10),Validators.maxLength(15)]),
      breed: this.fb.control<string>('', [Validators.required,Validators.maxLength(64)]),
      comments: this.fb.control<string>('',[Validators.required]),
      image: this.fb.control<string>('', [Validators.required])
    })
  }

  getUserId() {
    this.userId = this.localStorage.getItem("userid")
    console.info(this.userId)
  }



  processForm() {
    let value = this.form.value
    console.info(value)
    this.petBookletSvc.save(this.form, this.imageFile, this.userId).then((result) => {
      alert(result.success)
      this.router.navigate(['/MyInfo', this.username])
      

    }).catch((err) => { console.info(err) })
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