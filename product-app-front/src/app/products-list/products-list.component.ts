import { Component, Input } from '@angular/core';
import { Product } from '../product';

@Component({
  selector: 'app-products-list',
  imports: [],
  templateUrl: './products-list.component.html',
  styleUrl: './products-list.component.css'
})
export class ProductsListComponent {
  @Input() products: Product[] = [];
  @Input() reloadProucts: () => void = () => {};
  @Input() deleteProduct: (id: number) => void = () => {};
  @Input() setCurrentProduct: (product: Product) => void = () => {};

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
  constructor() {
    console.log('ProductsListComponent constructor');
  }
}
