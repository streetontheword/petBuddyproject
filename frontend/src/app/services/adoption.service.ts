import { HttpClient, HttpParams } from "@angular/common/http";
import { ElementRef, Injectable, inject } from "@angular/core";
import { BehaviorSubject, Observable, first, firstValueFrom, interval, switchMap } from "rxjs";
import { Appointment, ConfirmedInquiry, Dog, User, savedDog } from "../model";
import { FormGroup } from "@angular/forms";
import { DogStore } from "../Adoption/dog.store";

@Injectable()

export class AdoptionService {

    private http = inject(HttpClient)
    private store  = inject(DogStore)


    getDogsFromApi(): Promise<any> {

        return firstValueFrom(this.http.get("/api/dog"))

    }
//  24 * 60 * 60 * 1000;
    startScheduledApiCalls(): Observable<any> {
        const intervalTime =1 * 60 * 60 * 1000; // 1 hour in milliseconds
        return interval(intervalTime).pipe(
          switchMap(() => this.getDogsFromApi())
        );
      }
      
    getDogsFromMongo(page: number, pageSize: number): Observable<any> {
     
        const params = new HttpParams()
        .set('page', page)
        .set('pageSize', pageSize);

        return (this.http.get("/api/getDogs", {params}))

    }

    getDogsFromMongoWithoutPagination(): Observable<any>{
        return (this.http.get("/api/getAllDogsWithoutPagination"))
        
    }

    addDogsIntoMongo(form: FormGroup, imageFile: ElementRef): Promise<any>{
        const value = form.value

        const dataForm = new FormData()
        dataForm.set('name', value.name)
        dataForm.set('gender', value.gender)
        dataForm.set('age', value.age)
        dataForm.set('size', value.size)
        dataForm.set('coat', value.coat)
        dataForm.set('imageFile',imageFile.nativeElement.files[0])
        dataForm.set('description', value.description)
        dataForm.set('listOfCharacteristics', value.characteristics)
        dataForm.set('primaryBreed', value.primaryBreed)
        dataForm.set('secondaryBreed', value.secondaryBreed)
        dataForm.set('listOfColors', value.colors)
        dataForm.set('isMixed', value.mixed)
        dataForm.set('isGoodWithDogs', value.goodWithDogs)
        dataForm.set('isGoodWithCats', value.goodWithCats)
        dataForm.set('isGoodWithChildren', value.goodWithChildren)
        dataForm.set('isSpayedAndNeutered', value.spayedAndNeutered)
        dataForm.set('isHouseTrained',value.housetrained)
        dataForm.set('isDeclawed', value.declawed)
        dataForm.set('isSpecialNeeds', value.specialNeeds)
        dataForm.set('isVaccinated',value.vaccinated)
        return firstValueFrom(this.http.post<any>("/api/saveDogs",dataForm))

    }

    
    



    loadDogsIntoStore() {
        this.getDogsFromMongoWithoutPagination().subscribe((dogs: Dog[]) => {
          this.store.loadToStore(dogs);
          console.info("logging data into component store")
        })
        
    
      }
    getCountFromMongo():Observable<any>{
        return (this.http.get<any>("/api/getCount"))
    }

    getIndividualDogs(id: number): Promise<any> {
        return firstValueFrom(this.http.get(`/api/getDogs/${id}`))
    }

    favoriteDogs(userid: string, dog: savedDog): Promise<any> {
        return firstValueFrom(this.http.post<savedDog>(`/api/${userid}/save`, dog))
    }

    getFavorites(userid: string): Promise<any> {
        return firstValueFrom(this.http.get<savedDog>(`/api/${userid}/favorites`))
    }

    deleteSaved( petId: string, userid: string,): Promise<any>{
     
        return firstValueFrom(this.http.post<any>(`/api/${userid}/delete`, petId))
    }


    makeInquiry(userid: string, petid: number, url: string, petName: string, inquiryInfo: FormGroup): Promise<any> {
        const inquiryFullObject = {
            userid: userid, 
            petid: petid,
            url: url,
            petName: petName,
            inquiryid: '',
            firstName: inquiryInfo.value['firstName'],
            lastName: inquiryInfo.value['lastName'],
            email: inquiryInfo.value['email'],
            dateOfBirth: inquiryInfo.value['dateOfBirth'],
            gender: inquiryInfo.value['gender'],
            firstTimeOwner: inquiryInfo.value['firstTimeOwner'],
            nationality: inquiryInfo.value['nationality'], 
            other: inquiryInfo.value['other'], 
            dateOfViewing: inquiryInfo.value['dateOfViewing'],
            selectedHour: inquiryInfo.value['selectedHour']

        }
  

        return firstValueFrom(this.http.post<any>(`/api/${userid}/sendinquiry`, inquiryFullObject))
    }

    //email service 
    sendEmail(name: string, email: string, appointmentDate: string): Observable<any>{

        const inquiryObject = {
            email: email, 
            name: name, 
            appointmentDate: appointmentDate
        }

        return this.http.post<any>("/api/sendemail", inquiryObject)
    }


    getInquiry() : Observable<any>{
        return this.http.get<any>("/api/getinquiry")
    }

    getConfirmedInquiry() : Observable<any>{
        return this.http.get<Appointment[]>("/api/getconfirmed")
    }


    acceptInquiry(inquiryId: string, userid: string ): Promise<any>{
        return firstValueFrom(this.http.post<any>(`/api/${userid}/acceptinquiry`, inquiryId))

    }

    getIndividualAppointment(inquiryId: string ):Promise<ConfirmedInquiry>{

        return firstValueFrom(this.http.get<ConfirmedInquiry>(`/api/getIndividual/${inquiryId}`))

        
    }


    sendEmailConfirmation(name: string, email: string, appointmentDate: string): Observable<any>{
        const inquiryConfirmation = {
            email: email, 
            name: name, 
            appointmentDate: appointmentDate
        }
        return this.http.post<any>("/api/confirmation", inquiryConfirmation)
    }


    declineInquiry(inquiryId: string, userid: string ): Promise<any>{
        return firstValueFrom(this.http.post<any>(`/api/${userid}/declineinquiry`, inquiryId))

    }

    sendEmailDeclination(name: string, email: string, appointmentDate: string): Observable<any>{
        const inquiryDeclination = {
            email: email, 
            name: name, 
            appointmentDate: appointmentDate
        }
        return this.http.post<any>("/api/decline", inquiryDeclination)


    }
    private confirmedAppointmentsSource = new BehaviorSubject<Appointment[]>([]);
    confirmedAppointments$ = this.confirmedAppointmentsSource.asObservable();
  
  
    updateConfirmedAppointments(appointments: Appointment[]) {
      this.confirmedAppointmentsSource.next(appointments);
    }



    deleteAdoptedPet(id: number ): Promise<any>{

        return firstValueFrom(this.http.post<any>(`/api/${id}/remove`, id))

    }

}

