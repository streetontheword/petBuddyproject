import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { Observable } from "rxjs";
import { LocalStorageService } from "./local-storage-service";

@Injectable()

export class JwtInterceptor implements HttpInterceptor{
    

    private localStorage = inject(LocalStorageService)

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>>{
        let token = this.localStorage.getItem('token');
        if(token){
            request = request.clone({
                setHeaders: {
                    Authorization: `Bearer ${token}`
                }
            })
        }
        return next.handle(request)
    }


    
}