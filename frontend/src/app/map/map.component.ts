import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild, inject } from '@angular/core';
import { MatInput, MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field'
import { GoogleMapsModule, MapDirectionsService } from '@angular/google-maps';
import {  FormsModule } from '@angular/forms';
import { MatIcon } from '@angular/material/icon';
import { Router } from '@angular/router';


import { map } from 'rxjs';
import { PlaceSearchResult } from '../model';
import { MatButton } from '@angular/material/button';



@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrl: './map.component.css',
  standalone: true,
  imports: [CommonModule, MatFormFieldModule, MatInputModule, GoogleMapsModule, FormsModule, MatIcon, MatButton]

})

export class MapComponent implements OnInit,AfterViewInit{

@Input()
originPlaceholder = "Origin"


@Input()
destinationPlaceholder = "Destination"



@Output()
placeChanged = new EventEmitter<PlaceSearchResult> ()

@ViewChild('inputField') inputField!: ElementRef
@ViewChild('destination') destination!: ElementRef

private router = inject(Router)


//for autocomplete of locations
autocompleteorigin: google.maps.places.Autocomplete | undefined
autocompletedestination: google.maps.places.Autocomplete | undefined

//for the google maps

@ViewChild('mapContainer') mapContainer!: ElementRef
directionsService!: google.maps.DirectionsService;
directionsRenderer!: google.maps.DirectionsRenderer;



map!: google.maps.Map;


originPlaceId!: string  | undefined 

destinationPlaceId!: string  | undefined

mapCenter!: google.maps.LatLngLiteral;


og!: PlaceSearchResult
des!: PlaceSearchResult

  origin!: string;
  destination2!: string;
  distance!: string;
  duration!: string;

ngOnInit(): void {
  
    // Get user's current location
    this.getCurrentLocation()
   
  }

 

ngAfterViewInit(){
this.autocompleteorigin = new google.maps.places.Autocomplete(this.inputField.nativeElement)
this.autocompletedestination = new google.maps.places.Autocomplete(this.destination.nativeElement)


this.autocompleteorigin.addListener('place_changed', ()=>{
  const place = this.autocompleteorigin?.getPlace()
  // console.info(place)

  let originResult: PlaceSearchResult = {
    address : this.inputField.nativeElement.value, 
    location : place?.geometry?.location
    
  }

  this.og =originResult
  
//  console.info("origin>>>",originResult)

  this.originPlaceId = place?.place_id
  // console.info(this.originPlaceId)
  // console.info("this is the origin>>>", place?.place_id)
})

this.autocompletedestination.addListener('place_changed', ()=>{
  const place = this.autocompletedestination?.getPlace()
  this.destinationPlaceId = place?.place_id
  // console.info(this.destinationPlaceId)
  // console.info("this is the destination>>>", place?.place_id)

  let destinationResult: PlaceSearchResult = {
    address : this.destination.nativeElement.value, 
    location : place?.geometry?.location
    
  }
  this.des = destinationResult

//  console.info("destination",destinationResult)
})



}

makeInquiry(){
  this.router.navigate(['/dogsforadoption'])
}



getCurrentLocation(){
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(position => {
      this.mapCenter = {
        lat: position.coords.latitude,
        lng: position.coords.longitude
      };
    });
  } else {
    console.error('Geolocation is not supported by this browser.');
  }

}



calculateDistance(travelMode: string) {
  

  const service = new google.maps.DistanceMatrixService();
  service.getDistanceMatrix({
    origins: [{ placeId: this.originPlaceId }],
    destinations: [{ placeId: this.destinationPlaceId }],
    travelMode: travelMode as google.maps.TravelMode,
    unitSystem: google.maps.UnitSystem.METRIC,
  }, (response, status) => {
    if (status === 'OK' && response && response.rows && response.rows.length && response.rows[0].elements && response.rows[0].elements.length) {
      const result = response.rows[0].elements[0];
      this.distance = result.distance.text;
      this.duration = result.duration.text;
    } else {
      console.error('Error calculating distance:', status);
    }
  });
  this.getDirections()
}

constructor(private mapDirectionSvc: MapDirectionsService){}

//we are storing the results here 

directionsResult: google.maps.DirectionsResult | undefined

from !: PlaceSearchResult  |undefined
to!: PlaceSearchResult  |undefined


fromValue : PlaceSearchResult | undefined
toValue: PlaceSearchResult |undefined





getDirections() {
  // Check if this.og and this.des are defined and have a location property
  if (this.og && this.og.location && this.des && this.des.location) {
    const from = this.og.location;
    const to = this.des.location;
    // console.log("Getting directions from", from, "to", to);

    const request: google.maps.DirectionsRequest = {
      origin: from,
      destination: to,
      travelMode: google.maps.TravelMode.DRIVING
    };
    // console.info("REQUEST>>", request);

    this.mapDirectionSvc.route(request).pipe(
      map(res => res.result)
    ).subscribe((result) => {
      // console.info(result);
      this.directionsResult = result;
    });
  } else {
    console.error("Error: Unable to get valid 'from' and 'to' locations.");
  }
}


goHome(){
  this.router.navigate(['/home']);
}

}


