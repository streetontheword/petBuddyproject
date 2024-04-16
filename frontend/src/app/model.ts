

export interface PlaceSearchResult {
  address: string
  location: google.maps.LatLng | undefined
}


export interface User {
  email: string
  firstName: string
  lastName: string
  userName: string
  password: string

}

export interface AccountInfo {
  email: string
  firstName: string
  lastName: string
  userName: string
  url: string

}

export interface PersonalPet {
  petId: number
  userId: string
  name: string
  breed: string
  dateOfBirth: string
  dateOfLastVaccination: string
  gender: string
  imageUrl: string
  microchipNumber: string
  comments: string

}


export interface loginUser {
  email: string
  password: string
}


export interface savedDog {
  id: number
  petName: string
  secondaryBreed: string
  primaryBreed: string
  gender: string
  url: string
}

export interface Appointment {
  id: string
  startDate: Date;
  startTime: string;
  title: string;
  // other properties as needed
}


export interface MyCalendarEvent {
  
  inquiryId: string
  title: string
  startDate: Date
  selectedHour: string
  intended_visit_date: any
  firstName: string


}

export interface Thread {
  _id: string
  username: string
  title: string
  text: string
  userurl:string
  timestamp: string
  comments: Comment[]
}


export interface Comment {
  id: string
  username: string
  text: string
  timestamp: string
}

export interface ThreadSlice {
  threads: Thread[]
}


export interface Notification {
  username: string,
  text: string,
  id: string,
  read: boolean,
  timestamp: number
}

export interface Dog {
  id: string
  url: string
  name: string
  gender: string
}

export interface DogSlice {

  dogs: Dog[]

}

export interface ConfirmedInquiry{
  inquiryId: string 
  dogName: string 
  petId: string
  url: string
  firstName: string
  lastName: string
  email: string 
  firstTimeOwner: boolean
  intended_visit_date: string
  nationality: string 
  other: string
  birthdate: string
  userId: string

  
}
