import { Component, OnInit, inject } from '@angular/core';
import { ForumService } from '../services/forum.service';
import { LocalStorageService } from '../services/local-storage-service';
import { Notification } from '../model';



@Component({
  selector: 'app-notificationcontent',
  templateUrl: './notificationcontent.component.html',
  styleUrl: './notificationcontent.component.css'
})
export class NotificationcontentComponent implements OnInit {

  private forumSvc = inject(ForumService)
  private localStorage = inject(LocalStorageService)
  notificationArray: any[] = []
  username!: any

  ngOnInit(): void {
    this.username = this.localStorage.getItem("username")
    this.getNotifications(this.username)

  }


  getNotifications(username: any) {
    this.forumSvc.getNotifications(username).then(
      (result) => {
        console.info("RESULTS FROM BACKEDN", result)
        console.info(Array.isArray(result));
        this.notificationArray = result
      }

    ).catch((error) => { console.info("error fetching notifications", error) })
  }




  readNotification(notification: Notification, index:number) {

    if (!notification.read) {
      this.forumSvc.readNotification(notification.id).then(
        (result) => {
          console.info("what is this ", result)
          this.notificationArray.splice(index,1)
        }
        ).catch(
          () => alert("Error.")
          )
        }
        else {
         alert("unable to read notification")
    }
  }




}
