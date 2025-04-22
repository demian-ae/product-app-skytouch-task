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
  errorMessage: string | null = null;
  loading: boolean = false;

  constructor(private productService: ProductService) { 
    console.log('AppComponent constructor');
  }

  ngOnInit(): void{ 
    this.getProducts();
  }

  getProducts = () => {
    this.loading = true;
    this.productService.getProducts().subscribe({
      next: (data) => {
        console.log("Data length: " + data.length);
        this.products = data; 
        this.errorMessage = null; 
        this.loading = false;
      },
      error: (err) => {
        if (err.status === 0) {
          this.errorMessage = 'Cannot connect to the server. Please try again later.';
          this.loading = false;
          return;
        }
        const errorMsg = err.error?.message || 'An unexpected error occurred';
        console.error('Error getting products:', errorMsg);
        this.errorMessage = errorMsg;
        this.loading = false;
      }
    })
  }

  addProduct = (product: Omit<Product, 'id'>) => {
    this.loading = true;
    this.productService.addProduct(product).subscribe({
      next: (newProduct) => {
        console.log('Product added:', newProduct);
        this.getProducts();
        this.errorMessage = null;
        this.loading = false;
      },
      error: (err) => {
        if (err.status === 0) {
          this.errorMessage = 'Cannot connect to the server. Please try again later.';
          this.loading = false;
          return;
        }
        const errorMsg = err.error?.message || 'An unexpected error occurred';
        console.error('Error adding product:', errorMsg);
        this.errorMessage = errorMsg;
        this.loading = false;
      },
    });
  }

  setCurrentProduct = (product: Product) => {
    this.currentProduct = product; 
  }

  cleanCurrentProduct = () => {
    this.currentProduct = null;
  }

  saveEditedProduct = (product: Product) => {
    this.loading = true;
    this.productService.editProduct(product.id, product).subscribe({
      next: (updatedProduct) => {
        console.log('Product updated:', updatedProduct);
        this.getProducts();
        this.errorMessage = null;
        this.loading = false;
      },
      error: (err) => { 
        if (err.status === 0) {
          this.errorMessage = 'Cannot connect to the server. Please try again later.';
          this.loading = false;
          return;
        }
        const errorMsg = err.error?.message || 'An unexpected error occurred';
        console.error('Error saving edited product:', errorMsg);
        this.errorMessage = errorMsg;
        this.loading = false;
      }
    });
  }

  // Handle delete
  deleteProduct = (id: number) => {
    if (confirm('Are you sure you want to delete this product?')) {
      this.loading = true;
      this.productService.deleteProduct(id).subscribe(
        () => {
          this.products = this.products.filter(product => product.id !== id);
          this.getProducts();
          this.errorMessage = null;
          this.loading = false;
        },
        (err) => {
          if (err.status === 0) {
            this.errorMessage = 'Cannot connect to the server. Please try again later.';
            this.loading = false;
            return;
          }
          const errorMsg = err.error?.message || 'An unexpected error occurred';
          console.error('Error deleting product:', errorMsg);
          this.errorMessage = errorMsg;
          this.loading = false;
        },

      );
    }
  }
}
