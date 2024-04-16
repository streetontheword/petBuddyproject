import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { MapComponent } from './map/map.component';
import { userService } from './services/user.service';
import { Router } from '@angular/router';
import { LocalStorageService } from './services/local-storage-service';
import { MaterialModule } from './material/material.module';
import { AuthService } from './services/auth-service';
import { Subscription } from 'rxjs';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent implements OnInit, OnDestroy {
  title = 'frontend';

  private localStorage = inject(LocalStorageService)
  private router = inject(Router)
  private authService = inject(AuthService)
  isAdminSubscription!: Subscription;
  isAdmin!: any
  notifications: string[] = [];
  isLoggedIn!: boolean
  isAdminString!: any 

  ngOnInit(): void {


    // this.getUsername()
    // this.getAdminStatus()
    
    this.getEverything()
    this.getAdmin()
    // console.info("is admin logged in? ", this.authService.isAdminLoggedIn())


  }

  username!: any

  getEverything(){
    this.localStorage.getToken()
    this.username = this.localStorage.getItem("username")
    this.isAdminString = this.localStorage.getItem("role")
    // console.log("admin strng",this.isAdminString)
    if(this.isAdminString== "admin"){
   
      this.isAdmin = true
    }
    else{
 
      this.isAdmin = false
    }
  }

  getAdmin() {
    this.authService.isAdminSubject.subscribe({
      next: (response) => {
        // console.info("what response is this", response)
        this.isAdmin = response
        // console.info("boolean", this.isAdmin)
        if (this.isAdmin === "true") {
          this.localStorage.setItem("isAdmin", "true")
          this.setAdminToTrue()
        }
        this.localStorage.setItem("isAdmin", "false")
      }
    })
  }

  setAdminToTrue(){

    this.isAdminString = localStorage.getItem("isAdmin");
    this.isAdmin = this.isAdminString === "true";
  }


  getUsername() {
    this.username = this.localStorage.getItem("username")
  

  }


  logout() {
    this.localStorage.removeToken("token")
    this.localStorage.removeUsername("username")
    this.localStorage.removeisAdmin("isAdmin")
    this.localStorage.removeUserId("userid")
    this.localStorage.removeRole("role")
    this.localStorage.removeUrl("userurl")

  }


  navigateToFAQSection(): void {
    // Navigate to the other page

    this.router.navigate(['/']).then(() => {
      // After navigation, scroll to the FAQ section
      const element = document.getElementById('faq-sectionaboutPB');
      if (element) {
        element.scrollIntoView({ behavior: 'smooth', block: 'start', inline: 'nearest' });
      }
    });
  }

  ngOnDestroy(): void {
    this.isAdminSubscription.unsubscribe();
  }




}
