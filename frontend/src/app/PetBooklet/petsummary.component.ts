import { Component, OnInit, inject } from '@angular/core';
import { PetBooklet } from '../services/petbooklet-service';
import { Observable, Subscription } from 'rxjs';
import { LocalStorageService } from '../services/local-storage-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-petsummary',
  templateUrl: './petsummary.component.html',
  styleUrl: './petsummary.component.css'
})
export class PetsummaryComponent implements OnInit {


  private petSvc = inject(PetBooklet)
  private localStorage = inject(LocalStorageService)
  private route = inject(Router)
  userId!: any
  // sub$!: Subscription
  petArray: any[] = []
  username!: any
  

  ngOnInit(): void {
    
    this.getUserId()
    this.retrievePets(this.userId)
    this.username = this.localStorage.getItem("username")

  }



  getUserId(){
    this.userId = this.localStorage.getItem("userid")
  }
  

  retrievePets(userId: string) {
    this.petSvc.retrievePet(userId).then((result)=>{
      this.petArray = result
     
      
    })


  }

  press(petId: number){

    this.route.navigate(['/dog', petId])

  }

  deleteButton(petId: number){
   this.petSvc.deleteIndividualPet(petId, this.userId).then(
    (result)=>{
      alert(result.success)
      this.retrievePets(this.userId)
    }
   ).catch((err)=>{
    alert("unable to delete pet")
   })

  }

}