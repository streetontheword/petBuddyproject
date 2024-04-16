import { HttpClient, HttpParams } from "@angular/common/http";
import { ElementRef, Injectable, inject } from "@angular/core";
import { Observable, firstValueFrom, lastValueFrom } from "rxjs";
import { AccountInfo, User, loginUser } from "../model";

@Injectable()

export class userService{


private http = inject(HttpClient)


saveUser(user: User): Promise<any>{
    
return firstValueFrom(this.http.post<any>('/auth/saveUser',user))
}


login(user: any): Promise<any> {
    console.log(user)
    return firstValueFrom (this.http.post<any>('/auth/login', user))
}



getUser(userId: string): Promise<AccountInfo>{
    return firstValueFrom (this.http.get<AccountInfo>(`/auth/getUser/${userId}`))
}


updateDisplayPicture(imageFile: ElementRef, userId: string): Promise<any>{
    const updateForm = new FormData()
    updateForm.set('imageFile', imageFile.nativeElement.files[0])
    

    return firstValueFrom(this.http.post<any>(`/auth/${userId}/update`, updateForm))
    
}


getNotifications(userid:string): Promise<any> {
    // const url = "http://localhost:8080/notifications/get"
    const params = new HttpParams().set("userid",userid)
    return lastValueFrom(this.http.get<any>("/notifications/get", {params:params}))

  }


}