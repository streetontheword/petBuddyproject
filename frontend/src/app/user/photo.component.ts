import { Component, ElementRef, OnInit, ViewChild, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PetBooklet } from '../services/petbooklet-service';
import { LocalStorageService } from '../services/local-storage-service';
import { userService } from '../services/user.service';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-photo',
  templateUrl: './photo.component.html',
  styleUrl: './photo.component.css'
})
export class PhotoComponent implements OnInit {

  editDP!: FormGroup
  @ViewChild('file') imageFile!: ElementRef;


  private userSvc  = inject(userService)
  private localStorage = inject(LocalStorageService)
  private fb = inject(FormBuilder)
  private dialog = inject(MatDialog)

  userId!: any

  ngOnInit(): void {
    this.editDP = this.createForm()
    this.getUserId()
  }


  createForm(): FormGroup {
    return this.fb.group({
      image: this.fb.control<string>('', [Validators.required])
    })
  }

  getUserId(){
    this.userId = this.localStorage.getItem("userid")
    console.info(this.userId)
  }

  onSave() {
    
    this.userSvc.updateDisplayPicture(this.imageFile, this.userId).then((result) => {
      console.info(result)
    this.dialog.closeAll()

    })
  }
}
