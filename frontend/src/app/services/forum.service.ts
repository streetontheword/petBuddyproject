import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { FormGroup } from "@angular/forms";
import { Observable, lastValueFrom } from "rxjs";
import { LocalStorageService } from "./local-storage-service";
import { ForumStore } from "../forum/user/ForumStore";
import { Thread } from "../model";

@Injectable()

export class ForumService {


  private http = inject(HttpClient)
  private store = inject(ForumStore)

  private localStorage = inject(LocalStorageService)

  postNewThread(threadInfo: FormGroup, userurl: string): Observable<any> {

    const data = {
      id: '',
      username: this.localStorage.getItem("username"),
      title: threadInfo.value['title'],
      text: threadInfo.value['text'],
      timestamp: '',
      userurl: userurl,
      comments: []
    }

    return this.http.post("/api/forum/postNew", data);
  }

  //getting threads from backend 
  getThreads(): Observable<Thread[]> {
    return this.http.get<Thread[]>("/api/forum/threads");
  }


  //component store 
  loadThreadsIntoStore() {
    this.getThreads().subscribe((threads: Thread[]) => {
      console.info("instore>>>", threads)
      this.store.loadToStore(threads);
      console.info("logging data into component store")
    });

  }



  getThread(threadId: string): Observable<Thread> {

    let params = new HttpParams()
      .set('id', threadId);

    return this.http.get<Thread>("/api/forum/thread", { params });
  }


  postComment(threadId: string, comment: string): Observable<any> {
    const commentObject = {
      id: threadId,
      username: this.localStorage.getItem("username"),
      text: comment,
      timestamp: ''
    }
    console.info("commentobject",commentObject)
    return this.http.post<any>(`/api/forum/thread/${threadId}`, commentObject)
  }

  deleteComment(threadId: any): Observable<any> {

    return this.http.delete(`/api/forum/thread/${threadId}/delete`)

  }

  addNotification(payload:any): Promise<any> {
    // const url = this.notificationBaseUrl+"/add"
    return lastValueFrom(this.http.post<any>("/notification/add", payload))

  }

  getNumberOfUnreadNotifications(username:string): Promise<any>{
    
    const params = new HttpParams().set("username",username)
    return lastValueFrom(this.http.get<any>("/notification/unreadnumber",{params:params}))
  }
  

  getNotifications(username: string): Promise<any>{

    const params = new HttpParams().set("username",username)
    return lastValueFrom(this.http.get<any>("/notification/get", {params:params}))
  }

  readNotification(notifId: string){
    console.info(notifId)
    const params = new HttpParams().set("notifId", notifId)
    return lastValueFrom(this.http.get<any>("/notification/read",{params:params}))

  }

}