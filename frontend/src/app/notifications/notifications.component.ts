import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';

import { Observable, Subject, Subscription } from 'rxjs';
import { LocalStorageService } from '../services/local-storage-service';
import { HttpHeaders } from '@angular/common/http';
import { ForumService } from '../services/forum.service';


export const TYPE_SESSION_USERNAME = "session_username"

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrl: './notifications.component.css'
})
export class NotificationsComponent implements OnInit {

  unreadCount: number = 0
  sub!: Subscription

  webSocket!: WebSocket;
  username!:any
  private threadReplySubject = new Subject<string>
  private cdr = inject(ChangeDetectorRef)
  private localStorage = inject(LocalStorageService)
  wsUrlWithToken!: string
  private forumSvc = inject(ForumService)

  wsUrl = `wss://pets-are-friends.com/notif-websocket`;
  // `ws://localhost:8080/notif-websocket`
  

  ngOnInit(): void {
    console.info("in notifications component")
    this.getUserName()
    this.getNumberOfUnreadNotifications()
    

  }

  getUserName(){
    // to initialise everything
    this.username= this.localStorage.getItem("username")
    if(this.username !== ''){
      console.log("connected")
      this.connectToWebSocket()
      this.getNumberOfUnreadNotifications()
    }else{
      this.disconnectToWebSocket()
    }


    // this subscribes to the local storage subject
    // there will only be changes when user login/logout 
    // then the subject will push something out
    // nothing will be pushed during initialisation
    this.localStorage.usernameSubject.subscribe({
      next:(response)=>{
        this.username = response
        if(this.username!== ''){
          this.connectToWebSocket()
          this.getNumberOfUnreadNotifications()
        }
        else{
          this.disconnectToWebSocket()
           this.unreadCount = 0
        }
      }
    })
  }

  // connect() {
  //   console.info("Connecting to WebSocket...");
  //   this.webSocket = new WebSocket(this.wsUrl);

  //   this.webSocket.onopen = (event) => {
  //     console.info('WebSocket connection opened');

  //   };

  //   this.webSocket.onmessage = (event) => {
  //     console.info('Received message:', event.data);
  //     this.threadReplySubject.next(event.data); // Emit data to observer
  //   };

  //   console.info("WebSocket setup completed.");
  // }


  connectToWebSocket() {
    
    if ("WebSocket" in window) {
    
      this.webSocket = new WebSocket(this.wsUrl)
      this.webSocket.onopen = this.onWebSocketOpen.bind(this)
      this.webSocket.onmessage = this.onWebSocketMessage.bind(this)
      this.webSocket.onmessage = this.onReceive.bind(this)
      this.webSocket.onclose = this.onWebSocketClose.bind(this)


      this.webSocket.onerror = (error) => {
        console.error("Websocket error", error)
      }
    }
  }
  
  onWebSocketOpen() {
    console.info("websocket connected in app component")
    this.sendUserNameToServer()
 
  }

  onWebSocketMessage(evt: { data: string }) {
    let received_msg = JSON.parse(evt.data)
    console.info("received message", received_msg)
  }

  onWebSocketClose() {
    console.info("Websocket closed")
    setTimeout(() => {
      this.connectToWebSocket()
    }, 5000)
  
  }

  onReceive(event: MessageEvent<any>) {
    try {
      console.log("message received")
      let receivedMsg = JSON.parse(event.data)
      console.log(receivedMsg)
      let payload = receivedMsg.payload
      this.unreadCount = payload
      this.cdr.detectChanges()

    } catch (error) {
      console.info("error paarsing message", error)
    }
  
 }

  getThreadReplyNotifications(): Observable<string> {

    return this.threadReplySubject.asObservable();
  }

  
  disconnectToWebSocket(){
    if(this.webSocket){
      this.webSocket.close()
      console.log("closed")
    }else(
      console.error("No websocket connection")
    )
  }


  sendUserNameToServer(){
    if(this.webSocket && this.webSocket.readyState === WebSocket.OPEN){
      const payload={
        type:TYPE_SESSION_USERNAME,
        username:this.username
      }
      this.webSocket.send(JSON.stringify(payload))
      console.log("send username success")
    }
    else{
      console.error("failed to send message. might still be connecting")
    }
  }


  getNumberOfUnreadNotifications(){
    console.log("username before the query:", this.username)
    this.forumSvc.getNumberOfUnreadNotifications(this.username).then(
      (response)=>{
        console.info("response>>>",response)
        this.unreadCount = response.count
        console.info("number of unreadcount",this.unreadCount)
      }
    )
  }
}


