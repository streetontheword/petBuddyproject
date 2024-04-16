import { HttpClient } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ForumService } from '../../services/forum.service';
import { Observable, Subscription } from 'rxjs';
import { AuthService } from '../../services/auth-service';
import { ActivatedRoute } from '@angular/router';
import { Thread } from '../../model';
import { ForumStore } from './ForumStore';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-forum',
  templateUrl: './forum.component.html',
  styleUrl: './forum.component.css'
})
export class ForumComponent implements OnInit {

  private forumSvc = inject(ForumService)
  private authSvc = inject(AuthService)
  private store = inject(ForumStore)
  deletedThread$!: Subscription
  threads$!: Subscription
  threadsArray: any[] = []
  isAdmin!: boolean
  private activatedRoute = inject(ActivatedRoute)

  // threadInStore$!: Observable<Thread[]>
  threadInStore$: Thread[] = []
  threadInStoreSub!: Subscription

  private fb = inject(FormBuilder)
  searchBar!: FormGroup



  ngOnInit(): void {
    
    this.searchBar = this.createSearchBar()
    this.isAdmin = this.authSvc.isAdminLoggedIn()
    this.forumSvc.loadThreadsIntoStore() //data is loaded into store 

    this.threadInStoreSub = this.store.getAllThreads.subscribe((result) => {
      this.threadInStore$ = result

      // console.info("contents from store", this.threadInStore$)

    })



    // console.info(this.isAdmin)
    //this.getThreads()

  }




  // getThreads() {
  //   this.threads$ = this.forumSvc.getThreads().subscribe({
  //     next: ((result) => {
  //       console.info("this is the result",result)

  //       this.threadsArray = result
  //       console.info(this.threadsArray)
  //     }),
  //     error: ((err) => {
  //       console.info(err)
  //     }),
  //     complete: () => { this.threads$.unsubscribe() }
  //   })
  // }


  deletePost(threadId: string) {
    threadId = this.activatedRoute.snapshot.params['threadId'];

    this.deletedThread$ = this.forumSvc.deleteComment(threadId).subscribe({
      next: ((result) => {
   
      }),
      error: ((err) => {
        console.info(err)
      }),
      complete: () => { this.deletedThread$.unsubscribe() }
    })
  }


  sub$!: Subscription

  createSearchBar(): FormGroup {
    return this.fb.group({
      search: this.fb.control<string>('')
    })
  }

  processSearch() {
    let search:string = this.searchBar.value["search"]

    this.sub$ = this.store.getMatchingThreadsByTitle(search).subscribe({
      next: (result)=>{
 
        this.threadInStore$ =result
      }
    })

  }



  clearSearch() {
    this.searchBar.reset()
  }

  resetField(){

    this.threadInStoreSub = this.store.getAllThreads.subscribe((result) => {
      this.threadInStore$ = result



    })
  }

}
