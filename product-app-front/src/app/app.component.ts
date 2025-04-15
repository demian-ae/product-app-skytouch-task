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
  currentProduct: Partial<Product> | null = null;

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

  addProduct = (product: Omit<Product, 'id'>) => {
    this.productService.addProduct(product).subscribe({
      next: (newProduct) => {
        console.log('Product added:', newProduct);
        this.getProducts();
      },
      error: (err) => {
        console.error('Error adding product:', err);
      }
    });
  }

  setCurrentProduct = (product: Product) => {
    this.currentProduct = product; 
  }

  cleanCurrentProduct = () => {
    this.currentProduct = null;
  }

  saveEditedProduct = (product: Product) => {
    this.productService.editProduct(product.id, product).subscribe({
      next: (updatedProduct) => {
        console.log('Product updated:', updatedProduct);
        this.getProducts();
      },
      error: (err) => { 
        console.error('Error updating product:', err); 
      }
    })
  }

  // Handle delete
  deleteProduct = (id: number) => {
    if (confirm('Are you sure you want to delete this product?')) {
      this.productService.deleteProduct(id).subscribe(
        () => {
          this.products = this.products.filter(product => product.id !== id);
          this.getProducts();
        },
        (error) => {
          console.error('Error deleting product:', error);
        }
      );
    }
  }
}
