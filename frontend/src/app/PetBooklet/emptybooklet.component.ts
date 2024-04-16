import { Component, ElementRef, ViewChild, inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LocalStorageService } from '../services/local-storage-service';
import { PetBooklet } from '../services/petbooklet-service';

@Component({
  selector: 'app-emptybooklet',
  templateUrl: './emptybooklet.component.html',
  styleUrl: './emptybooklet.component.css'
})
export class EmptybookletComponent {


  @ViewChild('file') imageFile!: ElementRef;


  form!: FormGroup
  userId!: any
  username!: any
  petArray: any[] = []

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
      name: this.fb.control<string>('', [Validators.maxLength(128)]),
      dateOfBirth: this.fb.control<string>(('')),
      dateOfVaccination: this.fb.control<string>(('')),
      gender: this.fb.control<string>(('')),
      microChipNumber: this.fb.control<string>('', [Validators.maxLength(64)]),
      breed: this.fb.control<string>('', [Validators.maxLength(64)]),
      comments: this.fb.control<string>(''),
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
      location.reload()
      

    }).catch((err) => { console.info(err) })
  }



  

}

