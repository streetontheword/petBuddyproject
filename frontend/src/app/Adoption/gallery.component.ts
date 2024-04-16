import { Component, inject } from '@angular/core';
import { AdoptionService } from '../services/adoption.service';
import { Subscription } from 'rxjs';
import { DogStore } from './dog.store';
import { Dog } from '../model';
import { FormBuilder, FormGroup } from '@angular/forms';
import { AuthService } from '../services/auth-service';

@Component({
  selector: 'app-gallery',
  templateUrl: './gallery.component.html',
  styleUrl: './gallery.component.css'
})
export class GalleryComponent {

  private adoptionSvc = inject(AdoptionService)
  private store = inject(DogStore)
  private authSvc = inject(AuthService)

  dogArray: any[] = []
  totalItems!: number
  pageSize = 21;
  currentPage = 0
  dogsub$!: Subscription
  count$!: Subscription
  sub$!: Subscription
  isAdmin!: boolean
  allDogs$!: Subscription
  

  private fb = inject(FormBuilder)
  searchBar!: FormGroup

  options = [
    { label: 'Male', value: 'Male' },
    { label: 'Female', value: 'Female' }
  ];
  selectedOption!: string;

  ngOnInit(): void {

    this.isAdmin = this.authSvc.isAdminLoggedIn()
    this.fetchTotalItems()
    this.searchBar = this.createSearchBar()

    this.adoptionSvc.loadDogsIntoStore()
    this.store.getAllDogs.subscribe((result) => {
      // console.info("contents from store", result)
    })
  }

  createSearchBar(): FormGroup {
    return this.fb.group({
      search: this.fb.control<string>('')
    })
  }

  fetchTotalItems() {
    this.count$ = this.adoptionSvc.getCountFromMongo().subscribe({
      next: ((response) => {
        // console.info(response)
        this.totalItems = response
        this.getAllDogs(this.currentPage, this.pageSize)
      }),
      error: ((err) => { console.info("cannot get count", err) }),
      complete: () => { this.count$.unsubscribe() }
    })

  }

  // getAllDogsWithoutPagination(){
  //   this.allDogs$ = this.adoptionSvc.getDogsFromMongoWithoutPagination().subscribe({
  //     next: ((resp)=>{

  //     })
  //   })
  // }

  getAllDogs(currentPage: number, pageSize: number) {
   
    this.dogsub$ = this.adoptionSvc.getDogsFromMongo(currentPage, pageSize).subscribe({
      next: ((response) => {
        this.dogArray = response
      }),
      error: ((err) => { console.info("cannot get dogs", err) }),
      complete: () => { this.dogsub$.unsubscribe() }
    })


  }

  onPageChange(event: any) {
    // console.log('Page event:', event);
    this.currentPage = event.pageIndex;
    // console.info("new current page", this.currentPage, this.pageSize)
    this.getAllDogs(this.currentPage, this.pageSize);
  
  }

  onSelectionChange(event: any) {
    // console.log('Selected option:', event.value);
    this.searchByGender(event.value);
  }


  searchByGender(selectedOption: string): void {
    this.adoptionSvc.loadDogsIntoStore()
    // Perform the search action based on the selected option
    console.log('Performing search for:', selectedOption);
    this.sub$ = this.store.getMatchingDogByGender(selectedOption).subscribe({
      next: (result) => {
        // console.info("here is the filtered result>>>>>", result)
        this.dogArray = result
      }
    })

  }





  resetField() {
   this.getAllDogs(this.currentPage, this.pageSize)
  }

  processSearch() {
    let search: string = this.searchBar.value["search"]
    // console.info(search)
    this.sub$ = this.store.getMatchingDogByName(search).subscribe({
      next: (result) => {
        // console.info("here is the result>>>>>", result)
        this.dogArray = result
      }
    })

  }


  clearSearch() {
    this.searchBar.reset()
  }


  deletePost(petId: number){

    this.adoptionSvc.deleteAdoptedPet(petId).then((result)=>{
      // console.info(result)
      alert(result.deleted)
      this.getAllDogs(this.currentPage, this.pageSize)
    })
    .catch((err)=>{
      alert("Unsuccessful delete")
    })


  }




}
