import { Component, Input } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Product } from '../product';

@Component({
  selector: 'app-product-form',
  imports: [FormsModule],
  templateUrl: './product-form.component.html',
  styleUrl: './product-form.component.css'
})
export class ProductFormComponent {
  @Input() addProduct: (product: Omit<Product, 'id'>) => void = () => { };
  @Input() currentProduct: Partial<Product> | null = null;

  name: string = '';
  description: string = '';
  price: number | null = null;
  expirationDate: Date | null = null;

  ngOnChanges(): void {
    if (this.currentProduct) {
      this.name = this.currentProduct.name || '';
      this.description = this.currentProduct.description || '';
      this.price = this.currentProduct.price ?? null;
      this.expirationDate = this.currentProduct.expirationDate || null;
    }
  }

  onSubmit(event: Event): void {
    event.preventDefault();

    if (!this.name || !this.description || this.price === null) {
      alert('Please fill in all required fields before saving.');
      return;
    }

    const newProduct: Omit<Product, 'id'> = {
      name: this.name,
      description: this.description,
      price: this.price,
      expirationDate: this.expirationDate ? this.expirationDate : null,
    };

    this.addProduct(newProduct);

    // Optionally clear the form
    this.name = '';
    this.description = '';
    this.price = null;
    this.expirationDate = null;
  }

}
