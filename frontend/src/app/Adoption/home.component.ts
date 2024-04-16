import { Component, OnInit, inject } from '@angular/core';
import { AdoptionService } from '../services/adoption.service';
import { Subscription } from 'rxjs';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {


  panelOpenState = false;
  
  ngOnInit(): void {

  }


  // getAllDogs(){
  //   this.adoptionSvc.getDogsFromMongo().then((result)=>{
  //     console.info(result)
  //     this.dogArray = result
  //   })
  // }
}
