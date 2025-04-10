import { Component } from '@angular/core';
import { Product } from '../product';
import { ProductService } from '../product.service';

@Component({
  selector: 'app-products-list',
  imports: [],
  templateUrl: './products-list.component.html',
  styleUrl: './products-list.component.css'
})
export class ProductsListComponent {
  // mocke data
  products: Product[] = []

  formatDate = (date: Date | null): string => {
    if (date) {
      const day = String(date.getDate()).padStart(2, '0');
      const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are zero-based
      const year = date.getFullYear();
      return `${day}/${month}/${year}`;
    }
    return '';
  }

  constructor(private productService: ProductService) { }

  ngOnInit(): void{ 
    this.getProducts();
  }

  private getProducts(): void {
    this.productService.getProducts().subscribe(data => {
      this.products = data; 
    })
  }
}
