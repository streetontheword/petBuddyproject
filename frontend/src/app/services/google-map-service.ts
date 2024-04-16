import { Injectable } from '@angular/core';
import { GoogleMap } from '@angular/google-maps';


@Injectable({
  providedIn: 'root'
})
export class GoogleMapService {

  directionsObj!: google.maps.DirectionsResult
  markers: google.maps.Marker[] = []

  constructor() { }

  




}