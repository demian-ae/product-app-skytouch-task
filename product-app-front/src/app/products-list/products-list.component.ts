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
    if (!date) return '';
  
    let parsed: Date;
    
    // Spliting the date manually to avoid timezone shift
    if (typeof date === 'string') {
      const [year, month, day] = date.split('-');
      return `${day}/${month}/${year}`;
    }
  
    parsed = date;
    const day = String(parsed.getDate()).padStart(2, '0');
    const month = String(parsed.getMonth() + 1).padStart(2, '0'); // jan -> 0, feb -> 1 ...
    const year = parsed.getFullYear();
    return `${day}/${month}/${year}`;
  }

  constructor() {
    console.log('ProductsListComponent constructor');
  }
}
