import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, NavigationEnd} from '@angular/router';
import { environment } from '../../environments/environment';
import { loadStripe } from '@stripe/stripe-js';
import { HttpClient } from '@angular/common/http';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrl: './checkout.component.css'
})
export class CheckoutComponent implements OnInit {
  


  stripePromise = loadStripe(environment.stripe)
 
  private http = inject(HttpClient)

  donationForm!: FormGroup

  private router = inject(Router)
  private fb = inject(FormBuilder)

  
  paymentAmount!: number


  createForm(){
    return this.fb.group({
      name: this.fb.control<string>('', [Validators.required, Validators.maxLength(128)]),
      amount: this.fb.control<number>(1, [Validators.required])
    })
  }

  ngOnInit(): void {
  
    this.router.events
    .pipe(
      filter(event => event instanceof NavigationEnd)
    )
    .subscribe(() => {
      // Scroll to the top of the page
      window.scrollTo(0, 0);
    });
    this.donationForm = this.createForm()
    
  }



async pay(): Promise<void> {
  this.paymentAmount = this.donationForm.value['amount']
  // here we create a payment object
  const payment = {
    name: 'donation',
    currency: 'sgd',
    // amount on cents *10 => to be on dollar
    amount: this.paymentAmount * 100,
    cancelUrl: '/',
    successUrl: '/checkout/success',
  };

  
  const stripe = await this.stripePromise;

  this.http
     .post("https://pets-are-friends.com/api/payment", payment)
      .subscribe((data: any) => {
        // I use stripe to redirect To Checkout page of Stripe platform
        stripe?.redirectToCheckout({
          sessionId: data.id,
        });
      });
  }
}

// .post("https://pets-are-friends.com/api/payment", payment)


// http://localhost:8080/api/payment