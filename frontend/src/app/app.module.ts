import { ChangeDetectorRef, NgModule, isDevMode } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { LandingComponent } from './user/landing.component';
import { ReactiveFormsModule } from '@angular/forms';

import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { MapComponent } from './map/map.component';
import { paymentService } from './services/payment.service';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { RegistrationComponent } from './user/registration.component';
import { userService } from './services/user.service';
import { HomepageComponent } from './home/homepage.component';
import { PetbookletComponent } from './PetBooklet/petbooklet.component';
import { MaterialModule } from './material/material.module';
import { CancelComponent } from './stripe/cancel.component';
import { SuccessComponent } from './stripe/success.component';
import { CheckoutComponent } from './stripe/checkout.component';
import { LocalStorageService } from './services/local-storage-service';
import { PetBooklet } from './services/petbooklet-service';
// import { JwtInterceptor } from './services/Jwt-interceptor';
import { PetsummaryComponent } from './PetBooklet/petsummary.component';
import { IndividualpetComponent } from './PetBooklet/individualpet.component';
import { UpdatepetComponent } from './PetBooklet/updatepet.component';
import { JwtHelperService, JwtInterceptor, JwtModule } from '@auth0/angular-jwt';
import { HomeComponent } from './Adoption/home.component';
import { AdoptionService } from './services/adoption.service';
import { IndividualdogComponent } from './Adoption/individualdog.component';
import { InquiryformComponent } from './Adoption/inquiryform.component';
import { SavedSearchComponent } from './Adoption/saved-search.component';
import { AuthService } from './services/auth-service';
import { AccountinfoComponent } from './user/accountinfo.component';
import { ForumComponent } from './forum/user/forum.component';
import { NewthreadComponent } from './forum/user/newthread.component';
import { ThreadComponent } from './forum/user/thread.component';
import { ForumService } from './services/forum.service';
import { ForbiddenComponent } from './forbidden/forbidden.component';
import { DashboardComponent } from './forum/admin/dashboard.component';
import { CalendarModule, CalendarUtils, DateAdapter } from 'angular-calendar';
import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';
import { CalendarComponent } from './forum/admin/calendar.component';
import { DatePipe } from '@angular/common';
import { NotificationsComponent } from './notifications/notifications.component';
import { ForumStore } from './forum/user/ForumStore';
import { NotificationcontentComponent } from './notifications/notificationcontent.component';
import { CalendarService } from './services/calendar.service';
import { CalendarStore } from './forum/admin/CalendarStore';
import { PhotoComponent } from './user/photo.component';
import { PetPhotoComponent } from './PetBooklet/pet-photo.component';
import { GalleryComponent } from './Adoption/gallery.component';
import { DogStore } from './Adoption/dog.store';
import { AddDogsComponent } from './forum/admin/add-dogs.component';
import { EmptybookletComponent } from './PetBooklet/emptybooklet.component';
import { DetailsComponent } from './forum/admin/details.component';

@NgModule({
  declarations: [
    AppComponent,
    LandingComponent,
    RegistrationComponent,
    HomepageComponent,
    PetbookletComponent,
    CancelComponent,
    SuccessComponent,
    CheckoutComponent,
    PetsummaryComponent,
    IndividualpetComponent,
    UpdatepetComponent,
    HomeComponent,
    IndividualdogComponent,
    InquiryformComponent,
    SavedSearchComponent,
    AccountinfoComponent,
    ForumComponent,
    NewthreadComponent,
    ThreadComponent,
    ForbiddenComponent,
    DashboardComponent,
    CalendarComponent,
    NotificationsComponent,
    NotificationcontentComponent,
    PhotoComponent,
    PetPhotoComponent,
    GalleryComponent,
    AddDogsComponent,
    EmptybookletComponent,
    DetailsComponent
  
  ],
  imports: [
    BrowserModule,
    AppRoutingModule, 
    ReactiveFormsModule,
    CalendarModule,
    MapComponent,
    HttpClientModule,
    MaterialModule,
    CalendarModule.forRoot({ provide: DateAdapter, useFactory: adapterFactory }),
    JwtModule.forRoot({
      config: {
        tokenGetter: () => localStorage.getItem("token"),
        allowedDomains: ["localhost:4200", "localhost:8080"]
      }
    }),
    
  ],
  providers: [
    provideAnimationsAsync(), paymentService, userService, LocalStorageService, PetBooklet, AdoptionService, AuthService, ForumService, ForumStore, CalendarUtils,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptor,
      multi: true
    }, DatePipe, ForumStore, CalendarService, CalendarStore, DogStore
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
