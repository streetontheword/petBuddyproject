import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ForumService } from '../../services/forum.service';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';
import { LocalStorageService } from '../../services/local-storage-service';

@Component({
  selector: 'app-newthread',
  templateUrl: './newthread.component.html',
  styleUrl: './newthread.component.css'
})
export class NewthreadComponent implements OnInit{

  forumForm!: FormGroup

  private fb = inject (FormBuilder)
  private forumSvc = inject(ForumService)
  private localStorage = inject(LocalStorageService)
  private route = inject (Router)
  sub$!: Subscription
  userurl!: any

  ngOnInit(): void {

      this.forumForm = this.createForm()
      this.userurl = this.localStorage.getItem("userurl")
     
  }

  createForm(): FormGroup{
    return this.fb.group({
      title: this.fb.control<string>('', [Validators.required, Validators.minLength(5)]),
      text: this.fb.control<string>('', [Validators.required, Validators.minLength(5)])
    })
  }

  submitThread(){
    // const value = this.forumForm.value
    this.sub$ = this.forumSvc.postNewThread(this.forumForm, this.userurl).subscribe({
      next:((result)=>{
   
        this.route.navigate(['/forums'],)
      }),
      error:((err)=>{console.info(err)}),
      complete:()=>{this.sub$.unsubscribe()}
    })
  }




}

