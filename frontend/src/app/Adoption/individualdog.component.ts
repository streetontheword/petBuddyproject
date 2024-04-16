import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AdoptionService } from '../services/adoption.service';
import { MaterialModule } from '../material/material.module';
import { MatDialog } from '@angular/material/dialog';
import { InquiryformComponent } from './inquiryform.component';
import {  savedDog } from '../model';
import { LocalStorageService } from '../services/local-storage-service';

@Component({
  selector: 'app-individualdog',
  templateUrl: './individualdog.component.html',
  styleUrl: './individualdog.component.css'
})
export class IndividualdogComponent implements OnInit {

  private dialog = inject(MatDialog)
  private route = inject(ActivatedRoute)
  private adoptionSvc = inject(AdoptionService)
  private localStorage = inject(LocalStorageService)
  private router = inject(Router)
  

  petId!: number
  petName!: string
  individualDog: savedDog[] = []
  dogDetails: any[] = []
  gender!: string
  primaryBreed!: string
  secondaryBreed!: string
  userId!: any 
  url!: string

  ngOnInit(): void {
    // console.info("oninit")
    this.petId = this.route.snapshot.params['id']
    // console.info(this.petId)

    this.getIndividualDog(this.petId)
  }


  getIndividualDog(id: number) {
    this.adoptionSvc.getIndividualDogs(id).then((result) => {
      this.dogDetails.push(result)
      
      // console.info(this.userId)
      // console.info("individual dog>>>",result)
      //for the saved responses
    
              this.petName = result.name,
              this.gender = result.gender,
              this.primaryBreed = result.primaryBreed,
              this.petId = result.id
              this.url = result.url
      

    })
  }

  openDialog(): void {
    

    const dialogRef = this.dialog.open(InquiryformComponent, {
      data: {
        petId: this.petId,
        url: this.url,
        dogName: this.petName
      }
    });
    

    dialogRef.afterClosed().subscribe(result => {
    
      // Handle dialog close event
    });
  }

    addToFavorite() {
 
      this.userId = this.localStorage.getItem("userid")
      const dog: savedDog = {
        "id": this.petId,
        "petName": this.petName,
        "primaryBreed": this.primaryBreed,
        "secondaryBreed": this.secondaryBreed,
        "gender": this.gender, 
        "url": this.url
        
      }

      // console.info(this.userId, this.petId, this.gender, this.primaryBreed, this.secondaryBreed, this.petName, this.url)
      this.adoptionSvc.favoriteDogs(this.userId,dog).then((result)=>{
        // console.info(result)
        alert(result.success)
      })
      .catch((err)=>{
        // console.info(err)
        alert(err)
      })
    }

  
    navigateToFAQSection(): void {
      // Navigate to the other page
  
      this.router.navigate(['/adoption']).then(() => {
        // After navigation, scroll to the FAQ section
        const element = document.getElementById('faq-section');
        if (element) {
          element.scrollIntoView({ behavior: 'smooth', block: 'start', inline: 'nearest' });
        }
      });
    }
  
  

}
