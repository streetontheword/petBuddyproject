import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {MatSelectModule} from '@angular/material/select';
import {MatRadioModule} from '@angular/material/radio';
import {MatCardModule} from '@angular/material/card';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatTableModule} from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import {MatDialogModule} from '@angular/material/dialog';
import{MatToolbarModule} from '@angular/material/toolbar';
import { MatNativeDateModule } from '@angular/material/core';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatLuxonDateModule } from '@angular/material-luxon-adapter';
import { MatGridListModule } from '@angular/material/grid-list';
import {MatMenuModule} from '@angular/material/menu';
import {MatCheckboxModule} from '@angular/material/checkbox';
// import { MatMomentDateModule } from '@angular/material-moment-adapter';
import {MatBadgeModule} from '@angular/material/badge';
import {MatExpansionModule} from '@angular/material/expansion';



@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule,
    MatSelectModule,
    MatRadioModule,
    MatCardModule,
    MatDatepickerModule,
    MatTableModule,
    MatPaginator,
    MatDialogModule,
    MatToolbarModule,
    MatNativeDateModule,
    MatSlideToggleModule,
    MatLuxonDateModule,
    MatGridListModule,
    MatMenuModule,
    MatCheckboxModule,
    // MatMomentDateModule,
    MatBadgeModule, 
    MatExpansionModule

  ],
  exports: [   //must export the import so that it can be used throughout the application 
    CommonModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule,
    MatSelectModule,
    MatRadioModule,
    MatCardModule,
    MatDatepickerModule,
    MatTableModule,
    MatPaginator,
    MatDialogModule,
    MatToolbarModule,
    MatNativeDateModule,
    MatSlideToggleModule,
    MatLuxonDateModule,
    MatGridListModule,
    MatMenuModule,
    MatCheckboxModule,
    // MatMomentDateModule,
    MatBadgeModule,
    MatExpansionModule

  ]
})
export class MaterialModule { }
