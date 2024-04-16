import { Injectable } from "@angular/core";
import { ComponentStore } from "@ngrx/component-store";
import { Dog, DogSlice } from "../model";
import { Observable } from "rxjs";



const INIT: DogSlice = {
    dogs: []
}

@Injectable()

export class DogStore extends ComponentStore<DogSlice> {
    constructor() { super(INIT) }




    readonly getAllDogs = this.select<Dog[]>(
        (slice: DogSlice) => slice.dogs

    )


    readonly loadToStore = this.updater<Dog[]>(
        (_slice: DogSlice, values: Dog[]) => {
            return {
                dogs: values
            } as DogSlice
        }
    )

    readonly getMatchingDogByGender = (gender: string): Observable<Dog[]> =>
        this.select(state =>
            state.dogs.filter(dog =>
                dog.gender.includes(gender)
            )
        );

    readonly getMatchingDogByName = (search: string): Observable<Dog[]> =>
        this.select(state =>
            state.dogs.filter(dog =>
                dog.name.toLowerCase().includes(search.toLowerCase())
            )
        );

}

