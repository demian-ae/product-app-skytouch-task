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
  @Input() currentProduct: Partial<Product> | null = null;
  @Input() addProduct: (product: Omit<Product, 'id'>) => void = () => { };
  @Input() cleanCurrentProduct: () => void = () => { };
  @Input() saveEditedProduct: (product: Product) => void = () => { };

  name: string = this.currentProduct?.name || '';
  description: string = this.currentProduct?.description || '';
  price: number | null = this.currentProduct?.price || null;
  expirationDate: Date | null = this.currentProduct?.expirationDate || null;

  ngOnChanges(): void {
    if (this.currentProduct) {
      this.name = this.currentProduct.name || '';
      this.description = this.currentProduct.description || '';
      this.price = this.currentProduct.price || null;
      this.expirationDate = this.currentProduct.expirationDate || null;
    }
    console.log('ngOnChanges called', this.currentProduct);
  }

  onCancel = () => {
    this.cleanCurrentProduct();
    this.name = '';
    this.description ='';
    this.price = null;
    this.expirationDate = null;
  }

  onSubmit(event: Event): void {
    event.preventDefault();

    if (!this.name || !this.description || this.price === null || this.price < 0) {
      alert('Please fill in all required fields before saving.');
      return;
    }
    
    if(this.currentProduct?.id) {
      const updatedProduct: Product = {
        id: this.currentProduct.id,
        name: this.name,
        description: this.description,
        price: this.price,
        expirationDate: this.expirationDate ? this.expirationDate : null,
      };
      
      this.saveEditedProduct(updatedProduct);
      this.onCancel();
    } else { 
      const newProduct: Omit<Product, 'id'> = {
        name: this.name,
        description: this.description,
        price: this.price,
        expirationDate: this.expirationDate ? this.expirationDate : null,
      };
  
      this.addProduct(newProduct);
      this.onCancel();
    }
  }
}
