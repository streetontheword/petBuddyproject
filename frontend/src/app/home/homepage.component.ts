import { Component, OnInit, inject } from '@angular/core';
import { LocalStorageService } from '../services/local-storage-service';
import { Router } from '@angular/router';
import { AdoptionService } from '../services/adoption.service';
import { MaterialModule } from '../material/material.module';



@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrl: './homepage.component.css'
})
export class HomepageComponent implements OnInit {

  private localStorage = inject(LocalStorageService )
  private router = inject (Router)
  private adoptionSvc = inject(AdoptionService)





  ngOnInit(): void {
      // this.logout()
      // console.info("onint")
      this.getAllDogs()
  
  }
  process(){
    this.router.navigate(['/mypets'])
  }


  getAllDogs(){
    this.adoptionSvc.startScheduledApiCalls().subscribe({
      next: (result) =>{
          // console.info("calling api after 1 hr", result)
      },
    })
  }


  // getAllDogs(){
  //   this.adoptionSvc.getDogsFromApi().then(
  //     (result)=>{
  //       console.info(result)
  //     }
  //   )
  // }

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

  navigateToDonationSection():void{
    this.router.navigate(['/checkout']).then(() => {
      // After navigation, scroll to the FAQ section
      const element = document.getElementById('donation-picture');
      if (element) {
        element.scrollIntoView({ behavior: 'smooth', block: 'start', inline: 'nearest' });
      }
    });
  }

}
