import { HttpClient } from "@angular/common/http";
import { ElementRef, Injectable, inject } from "@angular/core";
import { FormGroup } from "@angular/forms";
import { Observable, firstValueFrom } from "rxjs";
import { savedDog } from "../model";

@Injectable()


export class PetBooklet{


    private http = inject(HttpClient)


    save(form: FormGroup, imageFile: ElementRef, userId: string ): Promise<any> {

       
        const value = form.value
        // console.info("form values>>>>",value)
        // console.info("SEE IMGAGE ELEMENT REF>>>>>>", imageFile.nativeElement.files[0])

        const dataForm = new FormData()
        dataForm.set('name', value.name)
        dataForm.set('dateOfBirth' , value.dateOfBirth)
        dataForm.set('dateOfVaccination', value.dateOfVaccination)
        dataForm.set('gender', value.gender)
        dataForm.set('microChipNumber', value.microChipNumber)
        dataForm.set('comments', value.comments)
        dataForm.set('breed', value.breed)
        dataForm.set('imageFile', imageFile.nativeElement.files[0])
       // console.info("BUILT FINISH >>>>>>", dataForm) 
    
        return firstValueFrom(this.http.post<any>(`/api/${userId}/save`, dataForm))

       
    }

    updateDisplayPicture(imageFile: ElementRef, userId: string, petId: number): Promise<any>{
        const updateForm = new FormData()

         updateForm.set('imageFile', imageFile.nativeElement.files[0])
      
    

        return firstValueFrom(this.http.post<any>(`/api/${userId}/${petId}/updatePicture`,updateForm))
        
    }

    updateFormFields(form: FormGroup, petId: number, userId: string): Promise<any>{
        // console.info("userid", userId)
        const value = form.value
        const updatedPetObject={
            petId: petId,
            userId: userId,
            name: value.name,
            dateOfBirth: value.dateOfBirth,
            dateOfVaccination: value.dateOfLastVaccination,
            gender: value.gender,
            microchipNumber: value.microchipNumber,
            comments: value.comments,
            breed: value.breed

        }
        return firstValueFrom(this.http.post<any>(`/api/${userId}/updateDetails`, updatedPetObject))

    }



    retrievePet(userId: string): Promise<any>{
        return firstValueFrom(this.http.get<any>(`/api/${userId}/pets`))
    }

    getIndividualPet(petId: number ): Promise<any>{
        return firstValueFrom(this.http.get<any>(`/api/${petId}`))
    }

    deleteIndividualPet(petId: number, userId: string): Promise<any> {
        // console.info(petId)
        // console.info(userId)
        return firstValueFrom(this.http.post<any>(`/api/${petId}/deletePet`, userId))  
    }


}