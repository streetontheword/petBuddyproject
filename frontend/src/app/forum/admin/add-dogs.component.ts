import { Component, ElementRef, OnInit, ViewChild, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AdoptionService } from '../../services/adoption.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-dogs',
  templateUrl: './add-dogs.component.html',
  styleUrl: './add-dogs.component.css'
})
export class AddDogsComponent implements OnInit{
  
  @ViewChild('file') imageFile!: ElementRef;

  
  addDogForm!: FormGroup
  private fb = inject(FormBuilder)
  private adoptionSvc = inject(AdoptionService)
  private router = inject(Router)
  
  
  ngOnInit(): void {
    
    this.addDogForm = this.createForm()

  }


  createForm(){
    return this.fb.group({
      name: this.fb.control<string>('', [Validators.required]),
      gender: this.fb.control<string>('',[Validators.required]),
      age: this.fb.control<string>('', [Validators.required]),
      size: this.fb.control<string>('', [Validators.required]),
      image: this.fb.control<string>('', [Validators.required]),
      description: this.fb.control<string>('', [Validators.required]),
      characteristics: this.fb.control<string>('', [Validators.required]),
      primaryBreed: this.fb.control<string>('', [Validators.required]),
      secondaryBreed: this.fb.control<string>(''),
      colors: this.fb.control<string>('',[Validators.required]),
      mixed: this.fb.control<boolean>(false),
      goodWithDogs: this.fb.control<boolean>(false),
      goodWithChildren: this.fb.control<boolean>(false),
      goodWithCats: this.fb.control<boolean>(false),
      spayedAndNeutered:this.fb.control<boolean>(false),
      housetrained: this.fb.control<boolean>(false),
      declawed: this.fb.control<boolean>(false),
      specialNeeds: this.fb.control<boolean>(false),
      vaccinated: this.fb.control<boolean>(false)
    })
  }

  // int id = dog.getId();
  // String name = dog.getName();
  // String gender = dog.getGender();
  // String age = dog.getAge();
  // String size = dog.getSize();
  // String coat = dog.getCoat();
  // List<String> listOfurl = dog.getUrl();
  // String published = dog.getPublished();
  // String description = dog.getDescription();
  // List<String> listOfCharacteristics = dog.getCharacteristics();
  // String primaryBreed = dog.getPrimaryBreed();
  // String secondaryBreed = dog.getSecondaryBreed();
  // // List<String> listOfBreeds = dog.getBreeds();
  // List<String> listOfColors = dog.getColor();
  // Boolean isMixed = dog.getIsMixed();
  // Boolean isUnknown = dog.getIsUnknown();
  // Boolean isGoodWithDogs = dog.getIsGoodWithDogs();
  // Boolean isGoodWithChildren = dog.getIsGoodWithChildren();
  // Boolean isGoodWithCats = dog.getIsGoodWithCats();
  // Boolean isSpayedAndNeutered = dog.getIsSpayedAndNeutered();
  // Boolean isHouseTrained = dog.getIsHouseTrained();
  // Boolean isDeclawed = dog.getIsDeclawed();
  // Boolean isSpecialNeeds = dog.getIsSpecialNeeds();
  // Boolean isVaccinated = dog.getIsVaccinated();

  processForm(){
   console.info(this.addDogForm.value)
   this.adoptionSvc.addDogsIntoMongo(this.addDogForm,this.imageFile).then(
   (result)=>{
    alert(result.success)
    this.router.navigate(['/dogsforadoption']);

   }
   ).catch((err)=>{
    alert("unsuccessfully added")

   })

  }


  // this.petBookletSvc.save(this.form, this.imageFile, this.userId).then((result)=>{
  //   console.info(result)
  // })







}
