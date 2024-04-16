import { Component, OnInit, inject } from '@angular/core';
import { LocalStorageService } from '../services/local-storage-service';
import { userService } from '../services/user.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { PhotoComponent } from './photo.component';
import { AccountInfo, User } from '../model';

@Component({
  selector: 'app-accountinfo',
  templateUrl: './accountinfo.component.html',
  styleUrl: './accountinfo.component.css'
})
export class AccountinfoComponent implements OnInit {
  private localStorage = inject(LocalStorageService)
  private userSvc = inject(userService)
  userid!: any
  accountinfo: any[] = []
  private dialog = inject(MatDialog)
  user!: AccountInfo
  username!: any

  ngOnInit(): void {
    this.userid = this.localStorage.getItem("userid")
    this.username = this.localStorage.getItem("username")

    console.info(this.userid)
    this.getUserInfo(this.userid)
    this.updateForm = this.createForm()


  }

  getUserInfo(userId: string) {
    this.userSvc.getUser(userId).then((result) => {
      // console.info(result)
       this.user  =  {
        userName : result.userName, 
        firstName : result.firstName,
        lastName : result.lastName,
        email: result.email,
        url : result.url
      }
      
      this.localStorage.setItem("userurl", this.user.url)
      // console.info("user obj" , this.user.url)

      // console.info(Array.isArray(result));
      // this.accountinfo.push(result)

    })
      .catch((err) => {
        console.info(err)
      })
  }


  updateForm!: FormGroup;
  editMode = false;

  private fb = inject(FormBuilder)


  openDialog(): void {
    console.info("dialog pressed")

    const dialogRef = this.dialog.open(PhotoComponent, {

    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      this.getUserInfo(this.userid)

    });
  }


  createForm(): FormGroup {
    return this.fb.group({
      userName: ['Initial Value'],
      email: ['Initial Value'],
      firstName: ['Initial Value'],
      lastName: ['Initial Value']
    });
  }

  editAccount(): void {
    this.editMode = true;
  }

  cancelEdit(): void {
    this.editMode = false;
    // Reset form to initial values
    // this.initForm();
  }

  saveChanges(): void {
    // Handle saving changes to backend
    // console.log(this.accountForm.value);
    // Reset edit mode
    this.editMode = false;
  }
}
