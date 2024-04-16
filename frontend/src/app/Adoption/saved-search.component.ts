import { Component, OnInit, inject } from '@angular/core';
import { AdoptionService } from '../services/adoption.service';
import { LocalStorageService } from '../services/local-storage-service';
import { savedDog } from '../model';

@Component({
  selector: 'app-saved-search',
  templateUrl: './saved-search.component.html',
  styleUrl: './saved-search.component.css'
})
export class SavedSearchComponent implements OnInit {


  private adoptionSvc = inject(AdoptionService)
  private localStorage = inject(LocalStorageService)
  userId!: any

  retrieveFavoriteList: any[] = []

  ngOnInit(): void {
    this.retrieveFavorites()
    this.userId = this.localStorage.getItem("userid")


  }

  retrieveFavorites() {
    this.userId = this.localStorage.getItem("userid")
    // console.info(this.userId)
    this.adoptionSvc.getFavorites(this.userId).then(
      (result) => {
        this.retrieveFavoriteList = result
      
      })
      .catch((err)=>{console.info(err)})
  }

  deletePet(petId: string) {

    this.adoptionSvc.deleteSaved(petId, this.userId).then(
      (result) => {
        alert(result.success)
        this.retrieveFavorites()
      }
    )
    .catch((err)=>{console.info(err)})


  }





}
