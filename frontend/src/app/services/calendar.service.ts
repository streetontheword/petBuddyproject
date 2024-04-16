import { Injectable, inject } from "@angular/core";
import { Observable } from "rxjs";
import { MyCalendarEvent } from "../model";
import { HttpClient } from "@angular/common/http";
import { CalendarStore } from "../forum/admin/CalendarStore";


@Injectable()

export class CalendarService{

private http = inject(HttpClient)




    getConfirmedInquiry() : Observable<any>{
        return this.http.get<MyCalendarEvent[]>("/api/getconfirmed")
    }



    

}