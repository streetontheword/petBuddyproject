import { Injectable } from "@angular/core";
import { Subject } from "rxjs";

@Injectable()

export class LocalStorageService{

  usernameSubject = new Subject<string>
  tokenSubject = new Subject<string>

  isAdminSubject = new Subject<string>



    setItem(key: string, value: string): void {
        localStorage.setItem(key, value);
        if(key === "username"){
          this.usernameSubject.next(value)
        } else {
          if (key === "isAdmin"){
            // console.info("isadmin",value)
            this.isAdminSubject.next(value)
          }
        }
         
        
      }
      


      getItem(key: string) {
        if(key === null){
            return Promise.resolve()
        }
        return localStorage.getItem(key);
      }

      getToken(): string | null{
        return localStorage.getItem("token")
      }

      removeToken(key:string){
        localStorage.removeItem(key)
      }

      removeUsername(key:string){
        localStorage.removeItem(key)
        this.usernameSubject.next('')
      }

      removeisAdmin(key: string){
        localStorage.removeItem(key)
      }

      removeIsLoggedIn(key: string){
        localStorage.removeItem(key)
      }

      removeUserId(key:string){
        localStorage.removeItem(key)
      }
      removeRole(key:string){
        localStorage.removeItem(key)
      }

      removeUrl(key:string){
        localStorage.removeItem(key)
      }
     
}