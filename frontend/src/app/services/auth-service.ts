import { Injectable, inject } from "@angular/core";
import { JwtHelperService } from "@auth0/angular-jwt";
import { LocalStorageService } from "./local-storage-service";
import { BehaviorSubject, Observable, Subject } from "rxjs";

@Injectable()

export class AuthService{


    jwtHelper: JwtHelperService = inject(JwtHelperService)
    private localStorage = inject(LocalStorageService)

    
    // isAdminSubject: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(true)
    isAdminSubject = new Subject<boolean> 

    isAdmin$: Observable<boolean> = this.isAdminSubject.asObservable()  

    getAccessToken(): string | null {
        // console.log(">> AuthService: Tried to retrieve ACCESS_TOKEN - " + localStorage.getItem("token"))
        return localStorage.getItem("token")
      }


    isAuthenticated(): boolean {
       const token =  this.getAccessToken()

        if (token === null) {
          return false
        }
    
        if (this.jwtHelper.isTokenExpired(token)) {
          console.log(">> AuthService: Token has expired! Deleting token...")
          this.clearToken()
          return false
        }
    
        console.log(">> AuthService: Token is not expired")
        return true
      }


      clearToken(): void {
        localStorage.removeItem("token")
      }
    

      getTokenRole(): any {
        
        const token = this.localStorage.getItem("token")
        if (!token) {
          return null;
        }
        const role = this.localStorage.getItem("role")
        console.info("IN authsvc>>> ", role)
        return role
      }


       isAdminLoggedIn(): boolean {

        if (this.getAccessToken() === null) {
          return false;
        }
    
        const role: string = this.getTokenRole();
        return role == "admin";
      }
    

       isUserLoggedIn(): boolean {
    
        if (this.getAccessToken() === null) {
          return false;
        }
    
        const role: string = this.getTokenRole();
        return role == "user";
      }

      updateUserRole(isAdmin: boolean): void {
        this.isAdminSubject.next(isAdmin);
      }



}