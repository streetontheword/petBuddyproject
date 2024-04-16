import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PetBooklet } from '../services/petbooklet-service';
import { LocalStorageService } from '../services/local-storage-service';
import { MatDialog } from '@angular/material/dialog';
import { PetPhotoComponent } from './pet-photo.component';
import { PersonalPet } from '../model';
import { UpdatepetComponent } from './updatepet.component';

@Component({
  selector: 'app-individualpet',
  templateUrl: './individualpet.component.html',
  styleUrl: './individualpet.component.css'
})
export class IndividualpetComponent implements OnInit{


  private route = inject(ActivatedRoute)
  
  private router = inject(Router)
  private petSvc = inject(PetBooklet)
  petId!: number 
  pet: any[] = []
  private dialog = inject(MatDialog)
  private localStorage = inject(LocalStorageService)
  username!: any
  dog!: PersonalPet 

  ngOnInit(): void {
      this.petId = this.route.snapshot.params['petId']
      // console.info(this.petId)
      this.retrievePet(this.petId)
      this.username = this.localStorage.getItem("username")
  }


  retrievePet(petId: number){
    this.petSvc.getIndividualPet(petId).then((result)=>{
      // console.info(result)
     this.dog = {
      petId: result.petId,
      userId: result.userId,
      name: result.name, 
      breed: result.breed,
      dateOfBirth: result.dateOfBirth,
      dateOfLastVaccination: result.dateOfLastVaccination,
      gender: result.gender,
      imageUrl: result.imageUrl,
      microchipNumber: result.microchipNumber,
      comments : result.comments
     }
      
    }).catch((err)=>{
      console.info("unable to retrieve", err)
      
    })
  }
 
   
  openDialog(): void {


    const dialogRef = this.dialog.open(PetPhotoComponent, {
      data: {
        petId: this.petId,
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      this.retrievePet(this.petId)

    

    });
  }

  openDetailsDialog():void{

    const dialogRef = this.dialog.open(UpdatepetComponent, {
      data: {
        petId: this.petId,
      }
    });

    dialogRef.afterClosed().subscribe(result => {

      this.retrievePet(this.petId)

    });
  }


  
  
}
