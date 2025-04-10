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

  formatDate = (date: string | Date | null): string => {
    if (date) {
      const parsed = typeof date === 'string' ? new Date(date) : date;
      const day = String(parsed.getDate()).padStart(2, '0');
      const month = String(parsed.getMonth() + 1).padStart(2, '0');
      const year = parsed.getFullYear();
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
