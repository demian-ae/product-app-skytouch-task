import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ProductsListComponent } from './products-list/products-list.component';
import { ProductFormComponent } from './product-form/product-form.component';
import { Product } from './product';
import { ProductService } from './product.service';


@Component({
  selector: 'app-root',
  imports: [RouterOutlet, ProductsListComponent, ProductFormComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  products: Product[] = []

  constructor(private productService: ProductService) { 
    console.log('AppComponent constructor');
  }

  ngOnInit(): void{ 
    this.getProducts();
  }

  getProducts = () => {
    this.productService.getProducts().subscribe(data => {
      console.log("Data length: " + data.length);
      this.products = data; 
    })
  }
}
