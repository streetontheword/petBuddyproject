import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LandingComponent } from './user/landing.component';
import { MapComponent } from './map/map.component';
import { RegistrationComponent } from './user/registration.component';
import { HomepageComponent } from './home/homepage.component';
import { PetbookletComponent } from './PetBooklet/petbooklet.component';
import { SuccessComponent } from './stripe/success.component';
import { CheckoutComponent } from './stripe/checkout.component';
import { CancelComponent } from './stripe/cancel.component';
import { PetsummaryComponent } from './PetBooklet/petsummary.component';
import { UpdatepetComponent } from './PetBooklet/updatepet.component';
import { IndividualdogComponent } from './Adoption/individualdog.component';
import { SavedSearchComponent } from './Adoption/saved-search.component';
import { authGuard } from './guards/auth.guard';
import { roleGuard } from './guards/route.guard';
import { loginGuard } from './guards/login.guard';
import { AccountinfoComponent } from './user/accountinfo.component';
import { ForumComponent } from './forum/user/forum.component';
import { ThreadComponent } from './forum/user/thread.component';
import { NewthreadComponent } from './forum/user/newthread.component';
import { IndividualpetComponent } from './PetBooklet/individualpet.component';
import { ForbiddenComponent } from './forbidden/forbidden.component';
import { DashboardComponent } from './forum/admin/dashboard.component';
import { CalendarComponent } from './forum/admin/calendar.component';
import { NotificationsComponent } from './notifications/notifications.component';
import { NotificationcontentComponent } from './notifications/notificationcontent.component';
import { HomeComponent } from './Adoption/home.component';
import { GalleryComponent } from './Adoption/gallery.component';
import { AddDogsComponent } from './forum/admin/add-dogs.component';


const routes: Routes = [
  {
    path: '', component: HomepageComponent,
  },
  {
    path: 'mypets', component: PetsummaryComponent, //underchild
    canActivate: [authGuard, roleGuard],
    data: {
      expectedRole: "user"
    }
  },
  {
    path: 'update/:petId', component: UpdatepetComponent,
    canActivate: [authGuard]
  },
  {
    path: 'petform', component: PetbookletComponent,
    canActivate: [authGuard]
  },
  {
    path: 'mypets/dog/:petId', component: IndividualpetComponent,
    canActivate: [authGuard, roleGuard],
    data: {
      expectedRole: "user"
    },
  },

  {
    path: 'adoption', component: HomeComponent

  },

  {
    path: 'dogsforadoption', component: GalleryComponent,
    canActivate: [authGuard]
  },

  {
    path: 'login', component: LandingComponent,
    canActivate: [loginGuard]
  },
  { path: 'registration', component: RegistrationComponent },


  {
    path: 'MyInfo/:username', component: AccountinfoComponent,
    canActivate: [authGuard]
  },

  {
    path: 'dogsforadoption/:id', component: IndividualdogComponent,
    canActivate: [authGuard]
  },


  { path: 'contact', component: MapComponent },

  {
    path: 'checkout', component: CheckoutComponent,
    // canActivate: [authGuard]
  },

  {
    path: 'checkout/success', component: SuccessComponent,
    // canActivate: [authGuard]
  },
  {
    path: 'checkout/cancel', component: CancelComponent,
    // canActivate: [authGuard]
  },

  //i dont get 
  { path: 'notifications', component: NotificationcontentComponent },

  //forums and threads
  {
    path: 'forums', component: ForumComponent,
    canActivate: [authGuard]
  },
  {
    path: 'forums/new', component: NewthreadComponent,
    canActivate: [authGuard]
  },
  {
    path: 'forums/:threadId', component: ThreadComponent,
    canActivate: [authGuard]
  },
  {
    path: 'addDog', component: AddDogsComponent,
    canActivate: [authGuard, roleGuard],
    data: {
      expectedRole: "admin"
    },
  },

  {
    path: 'dashboard', component: DashboardComponent, //underchild
    canActivate: [authGuard, roleGuard],
    data: {
      expectedRole: "admin"
    }

  },
  {
    path: 'calendar', component: CalendarComponent, //underchild
    canActivate: [authGuard, roleGuard],
    data: {
      expectedRole: "admin"
    }

  },


  {
    path: 'saved', component: SavedSearchComponent,
    canActivate: [authGuard]
  },

  { path: 'forbidden', component: ForbiddenComponent },

  { path: '**', redirectTo: '/', pathMatch: 'full' },



];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
