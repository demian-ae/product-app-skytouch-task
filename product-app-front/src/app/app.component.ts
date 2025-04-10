import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ProductsListComponent } from './products-list/products-list.component';
import { ProductFormComponent } from './product-form/product-form.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, ProductsListComponent, ProductFormComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'product-app-front';
}
