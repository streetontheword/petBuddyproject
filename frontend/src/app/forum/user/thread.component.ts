import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ForumService } from '../../services/forum.service';
import { Observable, Subscription } from 'rxjs';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth-service';

import { ForumStore } from './ForumStore';
import { Thread } from '../../model';
import { LocalStorageService } from '../../services/local-storage-service';


@Component({
  selector: 'app-thread',
  templateUrl: './thread.component.html',
  styleUrl: './thread.component.css'
})
export class ThreadComponent implements OnInit {

  private activatedRoute = inject(ActivatedRoute)
  private router = inject(Router)
  private authSvc = inject(AuthService)
  private forumSvc = inject(ForumService)
  private fb = inject(FormBuilder)
  private store = inject(ForumStore)
  private localStorage = inject(LocalStorageService)
  threadAuthor!: string
  comment !: Comment


  commentForm!: FormGroup
  showComments: boolean = false;

  deletedThread$!: Subscription
  isAdmin!: boolean
  thread$!: Subscription
  // thread$!: Observable<Tjhread> ;



  singleThread!: Thread
  threadId = this.activatedRoute.snapshot.params['threadId'];
  commentorAuthor!: any


  ngOnInit(): void {

    this.isAdmin = this.authSvc.isAdminLoggedIn()

    // this.showDetails(this.threadId)
    this.getIndividualThread(this.threadId)
    this.commentForm = this.createComment()

  }



  getIndividualThread(threadId: string) {
    this.thread$ = this.forumSvc.getThread(threadId).subscribe({
      next: ((result) => {
        // console.info("single thread result", result.title)
        this.threadAuthor = result.username

        this.singleThread  =  {
          _id: result._id,
          title : result.title, 
          username : result.username,
          text : result.text,
          comments: result.comments,
          userurl: result.userurl,
          timestamp: result.timestamp
        }
        // console.info("thread author", this.threadAuthor)
       
    

      }),
      error: ((err) => { console.info("unable to retrieve inidivual thread", err) }),
      complete: () => { this.thread$.unsubscribe() }
    })
  }


  createComment(): FormGroup {
    return this.fb.group({
      comment: this.fb.control<string>('', [Validators.required,Validators.minLength(5)])
    })
  }

  submitComment() {
    let value = this.commentForm.value['comment']
    this.commentorAuthor = this.localStorage.getItem("username")
    // console.info("commentor author", this.commentorAuthor)
    // console.info(value) 
    this.forumSvc.postComment(this.threadId, value).subscribe({
      next: ((result) => {
        // console.info("what result is this",result)
        alert(result.success)
        this.getIndividualThread(this.threadId)
        this.commentForm.reset()
      }),
      error: ((err)=>
        {console.info("there is an erro", err)
          alert("unable to post comments")
        })
      
    })
    if (this.threadAuthor != this.commentorAuthor) {
      // console.info("this is sent to notification", value)
      this.sendNotification(value)
     
    }
    
  }

  sendNotification(content: string) {

    const payload = this.createCommentNotification(content, this.threadAuthor, this.commentorAuthor, this.threadId)
    this.forumSvc.addNotification(payload).then(
      () => {
        // console.log("Notification success")
      }
    ).catch(
      () => {
        alert("Notification failed.")
      }
    )
  }


  deletePost() {

    this.deletedThread$ = this.forumSvc.deleteComment(this.singleThread?._id).subscribe({
      next: ((result) => {
        // console.info(result)
        alert(result.deleted)
        this.router.navigate(['/forums'])

      }),
      error: ((err) => {
        console.info(err)
      }),
      complete: () => { this.deletedThread$.unsubscribe() }
    })
  }

  createCommentNotification(content: string, to: string, from: string, id: string) {
    const text: string = from+" commented on your post: " + content
    // const endpoint = "/post/" + id
    const payload = {
      username: to,
      text: text,
    }
    // console.info(payload)
    return payload

  }

}


