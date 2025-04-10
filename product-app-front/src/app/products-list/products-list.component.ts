import { Component } from '@angular/core';
import { Product } from '../product';

@Component({
  selector: 'app-products-list',
  imports: [],
  templateUrl: './products-list.component.html',
  styleUrl: './products-list.component.css'
})
export class ProductsListComponent {
  // mocke data
  products: Product[] = [
    {
      id: 1,
      name: 'Product 1',
      description: 'Description of Product 1',
      price: 10.99,
      expirationDate: new Date('2023-12-31')
    },
    {
      id: 2,
      name: 'Product 2',
      description: 'Description of Product 2',
      price: 20.99,
      expirationDate: new Date('2024-01-15')
    },
    {
      id: 3,
      name: 'Product 3',
      description: 'Description of Product 3',
      price: 30.99,
      expirationDate: null
    }
  ]

  formatDate = (date: Date | null): string => {
    if (date) {
      const day = String(date.getDate()).padStart(2, '0');
      const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are zero-based
      const year = date.getFullYear();
      return `${day}/${month}/${year}`;
    }
    return '';
  }

}
