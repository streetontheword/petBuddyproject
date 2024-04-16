import { Component, ElementRef, ViewChild, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PetBooklet } from '../services/petbooklet-service';
import { LocalStorageService } from '../services/local-storage-service';
import { MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-pet-photo',
  templateUrl: './pet-photo.component.html',
  styleUrl: './pet-photo.component.css'
})
export class PetPhotoComponent {


  editDP!: FormGroup
  @ViewChild('file') imageFile!: ElementRef;


  private petBookletSvc = inject(PetBooklet)
  private localStorage = inject(LocalStorageService)
  private fb = inject(FormBuilder)
  private dialog = inject(MatDialog)
  private matDialog = inject(MAT_DIALOG_DATA)
  petid!: number


  userId!: any

  ngOnInit(): void {
    this.petid = this.matDialog.petId
    this.editDP = this.createForm()
    this.getUserId()
  }


  createForm(): FormGroup {
    return this.fb.group({
      image: this.fb.control<string>('', [Validators.required])
    })
  }

  getUserId() {
    this.userId = this.localStorage.getItem("userid")
    console.info(this.userId)
  }

  onSave() {

    this.petBookletSvc.updateDisplayPicture(this.imageFile, this.userId, this.petid).then((result) => {
      console.info(result)
      alert(result.success)
      this.dialog.closeAll()
    })
  }

}

